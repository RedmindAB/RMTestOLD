package se.redmind.utils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author Jeremy Comte
 */
public final class Fields {

    private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new LinkedHashMap<>();

    private Fields() {
    }

    public static Table<String, Object, Field> listByPathAndDeclaringInstance(Object instance) {
        Table<String, Object, Field> fieldsByPathAndDeclaringInstance = HashBasedTable.create();
        try {
            listByPathAndInstance(instance, fieldsByPathAndDeclaringInstance, "");
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            LoggerFactory.getLogger(Fields.class).error(ex.getMessage(), ex);
        }
        return fieldsByPathAndDeclaringInstance;
    }

    private static void listByPathAndInstance(Object instance, Table<String, Object, Field> fieldsByPathAndDeclaringInstance, String currentPath)
        throws IllegalArgumentException, IllegalAccessException {
        for (Field field : instance.getClass().getFields()) {
            fieldsByPathAndDeclaringInstance.put(currentPath + field.getName(), instance, field);
            Object value = field.get(instance);
            if (value != null) {
                Class<?> wrappedType = ClassUtils.primitiveToWrapper(field.getType());
                if (!wrappedType.getCanonicalName().startsWith("java.lang")) {
                    listByPathAndInstance(field.get(instance), fieldsByPathAndDeclaringInstance, currentPath + field.getName() + ".");
                }
            }
        }
    }

    public static void set(Object instance, String fieldName, Object value) {
        try {
            Fields.getField(instance.getClass(), fieldName).set(instance, value);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            LoggerFactory.getLogger(Fields.class).error(ex.getMessage(), ex);
        }
    }

    public static <E> E getSafeValue(Object instance, String fieldName) {
        try {
            return getValue(instance, fieldName);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Fields.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static <E> E getValue(Object instance, String fieldName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldsByNameOf(instance.getClass()).get(fieldName);
        if (field == null) {
            throw new NoSuchFieldException(fieldName + " doesn't exist on " + instance.getClass().getName());
        }
        return (E) field.get(instance);
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        Field field = getFieldsByNameOf(clazz).get(name);
        if (field == null) {
            throw new NoSuchFieldException(name + " doesn't exist on " + clazz.getName());
        }
        return field;
    }

    public static Map<String, Field> getFieldsByNameOf(Class<?> clazz) {
        Map<String, Field> fieldCache = FIELD_CACHE.get(clazz);
        if (fieldCache == null) {
            fieldCache = new LinkedHashMap<>();
            recursivelyCacheFieldsOf(clazz, fieldCache);
            FIELD_CACHE.put(clazz, fieldCache);
        }
        return fieldCache;
    }

    private static void recursivelyCacheFieldsOf(Class<?> clazz, Map<String, Field> fields) {
        for (Field field : clazz.getDeclaredFields()) {
            if (!fields.containsKey(field.getName())) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                fields.put(field.getName(), field);
            }
        }
        if (clazz.getSuperclass() != null) {
            recursivelyCacheFieldsOf(clazz.getSuperclass(), fields);
        }
    }
}