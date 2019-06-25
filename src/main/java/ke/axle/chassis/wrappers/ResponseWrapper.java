/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.axle.chassis.wrappers;

import java.io.Serializable;
import java.util.Date;

/**
 * Used to wrap JSON response
 * @author Cornelius M
 * @version 0.0.1
 * @author Owori Juma
 * @version 1.2.3
 * @param <T> data type
 */
public class ResponseWrapper <T> implements Serializable{  
    /**
     * HTTP Status code
     */
    private int code;
    /**
     * Response message
     */
    private String message;
    /**
     * Response data
     */
    private T data;
    /**
     * Response timestamp
     */
    private Long timestamp;
    
    /**
     * Default constructor assigns timestamp with current timestamp, default success message and status code 200
     */
    public ResponseWrapper(){
        this.code = 200;
        this.message = "Request was successful";
        this.timestamp = new Date().getTime();
    }

    /**
     * Get status code
     * @return 
     */
    public int getCode() {
        return code;
    }

    /**
     * Set status code
     * @param code 
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * get response message
     * @return 
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set response message
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get response data
     * @return 
     */
    public T getData() {
        return data;
    }

    /**
     * Set response data
     * @param data 
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Get response timestamp
     * @return 
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Set response timestamp
     * @param timestamp 
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ResponseWrapper{" + "code=" + code + ", message=" + message + ", data=" + data + ", timestamp=" + timestamp + '}';
    }
      
}
