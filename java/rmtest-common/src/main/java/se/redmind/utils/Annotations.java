package se.redmind.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;

/**
 * @author Jeremy Comte
 */
public class Annotations {

    public static <A extends Annotation> A defaultOf(Class<A> annotation) {
        return (A) Proxy.newProxyInstance(annotation.getClassLoader(), new Class<?>[]{annotation}, (proxy, method, args) -> {
            return method.getDefaultValue();
        });
    }
}
