/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.axle.chassis.utils;

/**
 * Used to handle logging requests and checking if maker is performing current requests
 * @author Cornelius M
 * @version 0.0.1
 * @author Owori Juma
 * @version 1.2.3
 */
public interface LoggerService {
    
    /**
     * Used to handle the logging request
     * @param description action description
     * @param entity entity being affected e.g. User
     * @param entityId entity ID i.e. value of the field annotated with {@link javax.persistence.Id}
     * @param activity activity being performed e.g. update, deletion, creation or deactivation
     * @param activityStatus activity status e.g. pending, approved
     * @param notes more information describing the action
     */
    public void log(String description, String entity, Object entityId, String activity, String activityStatus, String notes);
    
    /**
     * Used to check if the current user initiated the current action.
     * @param Entity entity being affected e.g. User
     * @param entityId entity ID i.e. value of the field annotated with {@link javax.persistence.Id}
     * @param activity activity being performed e.g. update, deletion, creation or deactivation
     * @return {@link true} if the current user is the initiator
     */
    public boolean isInitiator(String Entity, Object entityId, String activity);


    /**
     * Used to handle loggin request with User ID
     * @param description
     * @param entity
     * @param entityId
     * @param userId
     * @param activity
     * @param activityStatus
     * @param notes
     */
    public void log(String description, String entity, Object entityId, Long userId, String activity, String activityStatus, String notes);
}
