/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.axle.chassis.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Used to package error message into a list. By default sets the limit of error messages to 10
 * @author Cornelius M
 * @param <E> generic type of errors
 * @version 0.0.1
 * @author Owori Juma
 * @version 1.2.3
 */
public class ErrorList<E> extends ArrayList<E> {
    /**
     * Limits number of errors for this list
     */
    private int limit = 10;
    /**
     * Checks if the list capacity has been exceeded
     */
    private boolean hasOverflown = false;

    /**
     * Default constructor
     */
    public ErrorList() {
        super();
    }

    /**
     * Instantiates List with default capacity
     * @param initialCapacity List initial capacity
     */
    public ErrorList(int initialCapacity) {
        super(initialCapacity);
    }
    
    /**
     * Instantiate List with a collection
     * @param c
     */
    public ErrorList(Collection c) {
        super(c);
    }

    /**
     * Used to add elements to the list while enforcing the capacity (default is 10)
     * @param e Element to be added into the list
     * @return {@link true} if the operation was successful or {@link false} if capacity was exceeded
     */
    @Override
    public boolean add(E e) {
        if (limit == this.size()) {
            this.hasOverflown = true;
            return false;
        } else {
            return super.add(e);
        }
    }
    /**
     * Used to check if the array limit was exceeded
     * @return {@link true} if the list has been overflown
     */
    public boolean isOverflown() {
        return hasOverflown;
    }
    
    /**
     * Used to return list limit
     * @return list capacity 
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Used to set list limit/capacity
     * @param limit 
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

}
