package se.redmind.rmtest.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.reflectasm.MethodAccess;
import se.redmind.utils.UncheckedThrow;

/**
 * @author Jeremy Comte
 */
public final class Methods {

    private static final Logger LOGGER = LoggerFactory.getLogger(Methods.class);
    private static final HashMap<String, MethodAccessInstance> METHOD_CACHE = new LinkedHashMap<>();

    private Methods() {
    }

    public static void invoke(Object object, String method, Object... params) {
        try {
            MethodAccessInstance methodAccessCache = getFor(object.getClass(), method);
            if (params != null) {
                methodAccessCache.methodAccess.invoke(object, methodAccessCache.methodId, params);
            } else {
                methodAccessCache.methodAccess.invoke(object, methodAccessCache.methodId);
            }
        } catch (Exception e) {
            LOGGER.error("while invoking " + method + " on " + object, e);
            UncheckedThrow.throwUnchecked(e);
        }
    }

    private static MethodAccessInstance getFor(Class<?> clazz, String method) {
        String key = clazz.getName() + "." + method;
        MethodAccessInstance methodAccessCache = METHOD_CACHE.get(key);
        if (methodAccessCache == null) {
            MethodAccess methodAccess = MethodAccess.get(clazz);
            int methodId = methodAccess.getIndex(method);
            methodAccessCache = new MethodAccessInstance(methodAccess, methodId);
            METHOD_CACHE.put(key, methodAccessCache);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("caching " + clazz.getName() + "." + method + " [id:" + methodId + "], cache size: " + METHOD_CACHE.size());
            }
        }
        return methodAccessCache;
    }

    private static class MethodAccessInstance {

        public MethodAccess methodAccess;
        public int methodId;

        public MethodAccessInstance(MethodAccess methodAccess, int methodId) {
            this.methodAccess = methodAccess;
            this.methodId = methodId;
        }
    }
}
