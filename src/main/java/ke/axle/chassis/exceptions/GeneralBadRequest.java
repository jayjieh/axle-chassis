
package ke.axle.chassis.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when user input doesn't meet the necessary condition
 * @author Cornelius M
 * @version 0.0.1
 */
public class GeneralBadRequest extends Exception {
    /**
     * HTTP response status code
     */
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    /**
     * Default constructor
     */
    public GeneralBadRequest() {
        super();
    }

    /**
     * Instantiates class with default exception message
     * @param msg exception description
     */
    public GeneralBadRequest(String msg){
        super(msg);
    }
    
    /**
     * Instantiates class with default exception message and {@link HttpStatus}
     * @param msg exception description
     * @param status response status e.g. 417
     */
    public GeneralBadRequest(String msg, HttpStatus status){
        super(msg);
        this.status = status;
    }
    
    /**
     * Used to set the {@link HttpStatus}
     * @param status
     */
    public GeneralBadRequest setHttpStatus(HttpStatus status){
        this.status = status;
        return this;                
    }
    
    /**
     * Used to return {@link HttpStatus}
     * @return
     */
    public HttpStatus getHttpStatus(){
        return status;
    }
    
    
}
