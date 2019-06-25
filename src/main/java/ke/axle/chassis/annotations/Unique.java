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
 * Used to identify unique fields
 * <h3>Properties</h3>
 * <ul>
 * <li>fieldName</li>
 * </ul>
 * @author Cornelius M
 * @author Owori Juma
 * @version 1.2.3
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
    boolean isCaseSensitive() default false;
    /**
     * Meaningful field name used for logging and error response.
     * return String
     */
    String fieldName() default "Field";

}
