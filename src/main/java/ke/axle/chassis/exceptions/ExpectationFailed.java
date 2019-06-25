package ke.axle.chassis.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when certain conditions are not met
 * @author Cornelius M
 * @version 0.0.1
 */
public class ExpectationFailed extends GeneralBadRequest {

    /**
     * Default constructor
     */
    public ExpectationFailed() {
        super();
    }

    /**
     * Instantiates class with default exception message
     * @param msg exception description
     */
    public ExpectationFailed(String msg) {
        super(msg);
    }
    /**
     * Instantiates class with default exception message and {@link HttpStatus}
     * @param msg exception description
     * @param status response status e.g. 417
     */
    public ExpectationFailed(String msg, HttpStatus status) {
        super(msg, status);
    }

    
}
