package ke.axle.chassis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to edit enable edit on child entities record alone
 *
 * @author Owori Juma
 * @version 1.3.1
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChasisUUID {
    String name() default "";
}
