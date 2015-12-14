package se.redmind.rmtest.runners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jeremy Comte
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Parallelize {

    int threads() default -1;

    boolean drivers() default true;

    boolean tests() default false;

}
