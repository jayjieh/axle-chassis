/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.axle.chassis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used mark a field used to store edited entity name
 * @author Cornelius M.
 * @version 0.0.1
 * @author Owori Juma
 * @version 1.2.3
 */
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EditEntity {
    /**
     * Reserved for future use
     * return name as a {link String}
     */
    String name() default "";
}
