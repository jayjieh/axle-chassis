
package ke.axle.chassis.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when resource cannot be located
 * @author Cornelius M
 * @version 0.0.1
 */
public class NotFoundException extends GeneralBadRequest{
    /**
     * Default constructor
     */
    public NotFoundException(){
        super();
    }

    /**
     * Instantiates class with default exception message and {@link HttpStatus}
     * param msg exception description
     * param status response status e.g. 417
     */
    public NotFoundException(String msg, HttpStatus status) {
        super(msg, status);
    }
    
    /**
     * Instantiates class with default exception message
     * param message exception description
     */
    public NotFoundException(String message){
        super(message);
    }

}
