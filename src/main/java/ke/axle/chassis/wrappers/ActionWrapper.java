/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.axle.chassis.wrappers;

import java.util.Arrays;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Used to wrap checker action request
 * @author Cornelius M
 * @version 0.0.1
 * @author Owori Juma
 * @version 1.2.3
 * @param <T>
 */
public class ActionWrapper<T> {
    /**
     * Ids of entities to be affected
     */
    @NotNull
    @Size(min = 1, max = 1000)
    private T[] ids;
    /**
     * Action notes
     */
    @NotNull
    @Size(max = 255)
    private String notes;

    /**
     * Default constructor
     */
    public ActionWrapper() {
    }

    /**
     * Get ID(s)
     * return array of ID(s)
     */
    public T[] getIds() {
        return ids;
    }

    /**
     * Set entity ID(s)
     * param ids
     */
    public void setIds(T[] ids) {
        this.ids = ids;
    }

    /**
     * Get action notes
     * return a {link String) of notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set action notes
     * param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ActionWrapper{" + "ids=" + Arrays.toString(ids) + ", notes=" + notes + '}';
    }

}
