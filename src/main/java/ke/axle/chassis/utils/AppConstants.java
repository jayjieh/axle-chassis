/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.axle.chassis.utils;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Used to define constants to be used within the library
 * @author Cornelius M
 * @version 0.0.1
 * @author Owori Juma
 * @version 1.2.3
 */
public class AppConstants {
    public static final Marker AUDIT_LOG = MarkerFactory.getMarker("AUDIT_LOG");
    //activity type
    public static final String ACTIVITY_READ = "Read";
    public static final String ACTIVITY_CREATE = "Creation";
    public static final String ACTIVITY_UPDATE = "Update";
    public static final String ACTIVITY_DELETE = "Deletion";
    public static final String ACTIVITY_APPROVE = "Approve";
    public static final String ACTIVITY_VIEW = "View";
    public static final String ACTIVITY_DECLINE = "Decline";
    public static final String ACTIVITY_DEACTIVATE = "Deactivate";
    public static final String ACTIVITY_ACTIVATION = "Activation";
    public static final String ACTIVITY_SCHEDULE = "Schedule";
    public static final String ACTIVITY_CONFIRMATION = "Confirmation";
    public static final String ACTIVITY_FORWARD = "Forwarding";
    public static final String ACTIVITY_TALLYING = "Tallying";
    public static final String ACTIVITY_INITIATING = "Initiating";
    public static final String ACTIVITY_AUTHENTICATION = "Authentication";
    public static final String ACTIVITY_UNLOCK = "Unlock";
    public static final String ACTIVITY_LOCK = "Lock";
    public static final String ACTIVITY_AMEND = "Amend";
    //Record Status
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_INACTIVE = "Inactive";
    public static final String STATUS_LOCKED = "Locked";
    public static final String STATUS_DISABLED = "Disabled";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_UNAPPROVED = "Unapproved";
    public static final String STATUS_FAILED = "Failed";
    public static final String STATUS_NEW = "New";
    public static final String STATUS_DECLINED = "Rejected";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_EXPIRED = "Expired";
    public static final String STATUS_DEACTIVATED = "Deactivated";
    public static final String STATUS_VERIFIED = "Verified";
    public static final String STATUS_CONFIRMED = "Confirmed";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_UNCONFIRMED = "Unconfirmed";
    
    public static final String NO = "NO";
    public static final String YES = "YES";
    
    public static final String CHECKER_GENERAL_ERROR = "Some Actions could not be processed successfully check audit logs for more details";

}
