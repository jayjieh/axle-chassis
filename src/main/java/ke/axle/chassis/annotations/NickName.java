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
 * Used to provide user friendly name to an entity
 * @author Cornelius M
 * @version 0.0.1
 * @author Owori Juma
 * @version 1.2.3
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NickName {
    /**
     * name of the entity, if not specified record is used
     * return
     */
    public String name() default "Record";
}
