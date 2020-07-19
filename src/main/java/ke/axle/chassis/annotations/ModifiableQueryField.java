package ke.axle.chassis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark fields that can be used to query child entity
 *
 * @author Owori Juma
 * @version 1.4.1
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModifiableQueryField {
    public boolean isChained() default false;

    public Class<?> entityName() default Void.class;
}
