package se.redmind.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author Jeremy Comte
 */
public final class Fields {

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

}
