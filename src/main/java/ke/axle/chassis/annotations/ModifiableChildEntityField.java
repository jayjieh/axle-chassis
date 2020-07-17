package ke.axle.chassis.annotations;

import ke.axle.chassis.enums.ModifiableFieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to get modifiable details of Child Entity
 *
 * @author Owori Juma
 * @version 1.4.1
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModifiableChildEntityField {

    public Class<?> entityName() default Void.class;

    public ModifiableFieldType modifiableType() default ModifiableFieldType.QUERY;

    public String constantsValue() default "";

}
