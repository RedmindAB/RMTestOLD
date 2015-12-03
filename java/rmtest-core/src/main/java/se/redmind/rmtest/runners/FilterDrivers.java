package se.redmind.rmtest.runners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.Platform;

/**
 * @author Jeremy Comte
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FilterDrivers {

    Platform[] platforms() default {};

    Capability[] capabilities() default {};

}
