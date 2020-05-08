/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.axle.chassis.utils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import ke.axle.chassis.annotations.EditDataWrapper;
import ke.axle.chassis.annotations.EditEntity;
import ke.axle.chassis.annotations.EditEntityId;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.exceptions.ExpectationFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import javax.persistence.PersistenceContext;
//import org.springframework.stereotype.Component;

/**
 * Used to handle update requests (persisting changes in the edited entity, fetching changes,
 * declining changes and updating entity with the changes from edited entity)
 * <p>
 * param <T> entity affected by the changes
 * param <E> edited entity used to store changes temporarily before approval
 *
 * @author Cornelius M
 * @author Owori Juma
 * @version 1.2.3
 */
public class SupportRepository<T, E> {

    /**
     * Used to handle logging mainly to the console but you can implement an appender for more options
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * Entity mapping
     */
    private final Class<T> entityClazz;
    /**
     * Edited entity mapping
     */
    private final Class<E> editedEnClazz;
    /**
     * Entity manager used to handle persisting requests
     */
    private final EntityManager entityManager;
    /**
     * Criteria builder used to process HQL queries
     */
    private final CriteriaBuilder builder;

    /**
     * Used to instantiate the class
     * <p>
     * param entityManager   entity manager to handle transactions
     * param entityMapping   entity mapping
     * param editedEnMapping edited entity mapping
     */
    public SupportRepository(EntityManager entityManager, Class<T> entityMapping, Class<E> editedEnMapping) {
        this.entityManager = entityManager;
        this.builder = entityManager.getCriteriaBuilder();
        this.entityClazz = entityMapping;
        this.editedEnClazz = editedEnMapping;
    }

    /**
     * Checks if changes were made if true it persists changes to entity
     * storage. This method is different from super class since changes don't
     * affect the original entity they are saved in the edit entity first
     */
    public List<String> handleEditRequest(T entity, T oldEntity, Class<E> editEntity) throws
            IllegalAccessException, JsonProcessingException, ExpectationFailed {

        Serializable index = null;
        List<String> changes = new ArrayList<>();

        if (null != entity) {
            //Check if entity has been modified
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (field.isAnnotationPresent(Id.class)) {
                    index = (Serializable) field.get(entity);
                }
            }
            if (index == null) {
                log.warn("Failed to find id field on entity {} during handling edit request", entity);
            } else {
                changes = this.fetchChanges(entity, oldEntity);
                //If there are changes, update this field
                if (!changes.isEmpty()) {
                    updateChanges(index, entity, oldEntity, editEntity);
                }
            }
        }

        return changes;
    }

    public String getBeforeAndAfterValues(T oldEntity, T newEntity) {
        String beforeValues = " Before : ";
        PropertyAccessor oldAccessor = PropertyAccessorFactory.forBeanPropertyAccess(oldEntity);
        for (Field field : oldEntity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ModifiableField.class)) {
                beforeValues += field.getName() + " - " + oldAccessor.getPropertyValue(field.getName()) + " \n";
            }
        }
        String afterValues = " After : ";
        PropertyAccessor newAccessor = PropertyAccessorFactory.forBeanPropertyAccess(newEntity);
        for (Field field : newEntity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ModifiableField.class)) {
                afterValues += field.getName() + " - " + newAccessor.getPropertyValue(field.getName()) + " \n";
            }
        }
        return beforeValues + " and " + afterValues;
    }

    private void updateChanges(Serializable index, T entity, T oldEntity, Class<E> editEntity) throws
            IllegalAccessException, JsonProcessingException, ExpectationFailed {

        BeanWrapper wrapper = new BeanWrapperImpl(editEntity);
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Object.class, DynamicMixIn.class);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //ignore entities
        Set<String> ignoreProperties = new HashSet<>();
        for (Field field : oldEntity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToOne.class)) {
                ignoreProperties.add(field.getName());
            }
        }

        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("dynamicFilter", SimpleBeanPropertyFilter.serializeAllExcept(ignoreProperties));
        mapper.setFilterProvider(filters);

        String data = mapper.writeValueAsString(entity);
        CriteriaDelete c = this.builder.createCriteriaDelete(editEntity);
        Root criteriaRoot = c.from(editEntity);
        Predicate[] preds = new Predicate[2];
        for (Field field : editEntity.getDeclaredFields()) {
            if (field.isAnnotationPresent(EditEntity.class)) {
                wrapper.setPropertyValue(field.getName(), entity.getClass().getSimpleName());
                preds[0] = this.builder.equal(criteriaRoot.get(field.getName()), entity.getClass().getSimpleName());
            } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                wrapper.setPropertyValue(field.getName(), data);
            } else if (field.isAnnotationPresent(EditEntityId.class)) {
                if (field.getType().isAssignableFrom(String.class)) {
                    wrapper.setPropertyValue(field.getName(), index.toString());
                    preds[1] = this.builder.equal(criteriaRoot.get(field.getName()), index.toString());
                } else {
                    wrapper.setPropertyValue(field.getName(), index);
                    preds[1] = this.builder.equal(criteriaRoot.get(field.getName()), index);
                }

            }
        }

        //delete existing records to avoid data redudancy
        if (preds[0] != null && preds[1] != null) {
            c.where(preds);
            this.entityManager.createQuery(c).executeUpdate();
        } else {
            throw new ExpectationFailed("Failed to update editted edit "
                    + "due to either the fields annotated with @EditDataWrapper, @EditEntityId and @EditEntity could not be found");
        }
        E e = (E) wrapper.getWrappedInstance();
        this.entityManager.persist(e);
    }

    /**
     * Used to fetch { List} of changes
     * <p>
     * param newbean updated entity
     * param oldbean old entity
     * return a {link List} of {link String} changes
     * throws IllegalAccessException if fields with changes are not accessible
     */
    public List<String> fetchChanges(T oldbean, T newbean) throws IllegalAccessException {
        List<String> changes = new ArrayList<>();
        if (newbean.getClass() != oldbean.getClass()) {
            log.error(AppConstants.AUDIT_LOG, "Failed to fetch changes for {} and {}. "
                    + "Beans are not of the same class", oldbean.getClass(), newbean.getClass());
            return changes;
        }

        final Field[] allFields = oldbean.getClass().getDeclaredFields();
        for (Field field : allFields) {
            //Manage the fields that we need only
            if (field.isAnnotationPresent(ModifiableField.class)) {
                //Enable access of this field
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object _newValue = field.get(oldbean);
                Object _oldValue = field.get(newbean);

                if (_newValue != _oldValue) {
                    if ((_newValue != null && !_newValue.equals(_oldValue))
                            || (_oldValue != null && !_oldValue.equals(_newValue))) {
                        if (_oldValue == null) {
                            changes.add("Assigned " + _newValue + " to " + SharedMethods.splitCamelString(field.getName()));
                        } else {
                            changes.add(SharedMethods.splitCamelString(field.getName())
                                    + " changed from " + _oldValue + " to " + _newValue);
                        }
                    }
                }
            }
        }
        return changes;
    }

    /**
     * Used to fetch { List} of changes
     * <p>
     * param id entity id
     * param t  the old entity
     * return a {link List} of {link String} changes
     * throws java.lang.IllegalAccessException if fields with changes are not accessible
     * throws java.io.IOException              if changes cannot be mapped back to entity
     */
    public List<String> fetchChanges(Serializable id, T t) throws IllegalAccessException, IOException {
        List<String> changes = new ArrayList<>();

        if (t == null) {
            log.warn("Failed to find entity with id {} returning a List of empty changes", id);
            return changes;
        }

        CriteriaQuery<E> c = this.builder.createQuery(editedEnClazz);
        String dataField = null;
        Root criteriaRoot = c.from(editedEnClazz);
        Predicate[] preds = new Predicate[2];

        for (Field field : editedEnClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(EditEntity.class)) {
                log.debug("Adding restriction for field {} value {}", field.getName(), this.entityClazz.getSimpleName());
                preds[0] = this.builder.equal(criteriaRoot.get(field.getName()), this.entityClazz.getSimpleName());
            } else if (field.isAnnotationPresent(EditEntityId.class)) {
                if (field.getType().isAssignableFrom(String.class)) {
                    preds[1] = this.builder.equal(criteriaRoot.get(field.getName()), id.toString());
                } else {
                    preds[1] = this.builder.equal(criteriaRoot.get(field.getName()), id);
                }

            } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                dataField = field.getName();
            }
        }

        c.where(preds);
        E e;
        try {
            e = (E) this.entityManager.createQuery(c).getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            log.warn("Failed to find changes on returning a list of empty changes");
            return changes;
        }
        if (e == null) {
            log.warn("Failed to find changes on edited entity {} returning a list of empty changes", e);
            return changes;
        }
        String data;
        BeanWrapper wrapper = new BeanWrapperImpl(e);
        data = (String) wrapper.getPropertyValue(dataField);

        if (null != data) {
            //Serialize object
            ObjectMapper mapper = new ObjectMapper();
            T newbean = (T) mapper.readValue(data, this.entityClazz);
            return this.fetchChanges(newbean, t);
        }

        return changes;
    }

    /**
     * Fetch entity excluding entities in trash
     * <p>
     * param id entity id
     * return persistent context entity
     */
    public T fetchEntity(Serializable id) {
        //get id field name
        String fieldId = null;
        boolean hasIntrash = false;
        for (Field field : entityClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                fieldId = field.getName();
            }
            if (field.getName().equalsIgnoreCase("intrash")) {
                hasIntrash = true;
            }
        }

        if (fieldId == null) {
            throw new RuntimeException("Entity doesn't have an id field");
        }

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClazz);
        Root<T> root = criteriaQuery.from(entityClazz);
        if (hasIntrash) {
            criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get(fieldId), id),
                    criteriaBuilder.equal(root.get("intrash"), AppConstants.NO)));
        } else {
            criteriaQuery.where(criteriaBuilder.equal(root.get(fieldId), id));
        }
        try {
            return this.entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

    /**
     * Used to merge entity from Storage. Updates new values from edited record
     * to the provided entity
     */
    public T mergeChanges(Serializable id, T t) throws IOException, IllegalArgumentException, IllegalAccessException {

        String data, dataField = null;
        CriteriaQuery<E> c = this.builder.createQuery(editedEnClazz);
        Root criteriaRoot = c.from(editedEnClazz);
        Predicate[] preds = new Predicate[2];

        for (Field field : editedEnClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(EditEntity.class)) {
                preds[0] = this.builder.equal(criteriaRoot.get(field.getName()), this.entityClazz.getSimpleName());
            } else if (field.isAnnotationPresent(EditEntityId.class)) {
                if (field.getType().isAssignableFrom(String.class)) {
                    preds[1] = this.builder.equal(criteriaRoot.get(field.getName()), id.toString());
                } else {
                    preds[1] = this.builder.equal(criteriaRoot.get(field.getName()), id);
                }

            } else if (field.isAnnotationPresent(EditDataWrapper.class)) {
                dataField = field.getName();
            }
        }

        c.where(preds);
        E e;
        try {
            e = (E) this.entityManager.createQuery(c).getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            log.warn("Changes not found for entity {} returning current entity", t);
            return t;
        }

        if (e == null) {
            log.warn("Changes not found for entity {} returning current entity", t);
            return t;
        }
        BeanWrapper wrapper = new BeanWrapperImpl(e);
        data = (String) wrapper.getPropertyValue(dataField);
        if (null != data) {
            //Serialize object
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            T newBean = mapper.readValue(data, this.entityClazz);
            log.debug("Retrieved entity {} from data ({})", newBean, data);
            newBean = this.updateEdit(newBean, t);
            this.entityManager.remove(e);
            this.entityManager.merge(newBean);
            this.entityManager.flush();
            return newBean;
        } else {
            log.warn("Data field is empty returning current entity", t);
        }
        return t;
    }

    /**
     * Update entity with changes from the new object
     * <p>
     * param oldbean
     * param newbean
     * return updated bean
     * throws IllegalAccessException occurs when the field annotated with
     * {link ModifiableField} is not accessible
     */
    public T updateEdit(T newbean, T oldbean) throws IllegalAccessException {
        final Field[] allFields = newbean.getClass().getDeclaredFields();
        BeanWrapper wrapper = new BeanWrapperImpl(oldbean);
        for (Field field : allFields) {
            //Manage the fields that we need only
            if (field.isAnnotationPresent(ModifiableField.class)) {
                //Enable access of this field
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object oldValue = wrapper.getPropertyValue(field.getName());//field.get(oldbean);
                Object newValue = field.get(newbean);
                if (oldValue != newValue && newValue != null) {
                    wrapper.setPropertyValue(field.getName(), newValue);
                }
                if (oldValue != newValue && newValue != null) {
                    wrapper.setPropertyValue(field.getName(), newValue);
                }
            }
        }
        return oldbean;
    }

    /**
     * Used to decline entity changes. It clears data from the EditEntity
     * <p>
     * param id entity id
     * throws java.lang.IllegalAccessException if modified fields cannot be accessed
     */
    public void declineChanges(Serializable id) throws IllegalArgumentException, IllegalAccessException {
        E e = this.getEditedEntity(id);
        log.debug("Found edited record entity {}", e);
        if (e == null) {
            return;
        }
        this.entityManager.remove(e);
        this.entityManager.flush();
    }

    /**
     * Get edited entity with changes
     *
     * @param entityId edited entity id
     * @return
     */
    private E getEditedEntity(Serializable entityId) {
        CriteriaQuery<E> c = this.builder.createQuery(editedEnClazz);
        Root criteriaRoot = c.from(editedEnClazz);
        Predicate[] preds = new Predicate[2];

        for (Field field : editedEnClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(EditEntity.class)) {
                preds[0] = this.builder.equal(criteriaRoot.get(field.getName()), this.entityClazz.getSimpleName());
            } else if (field.isAnnotationPresent(EditEntityId.class)) {
                if (field.getType().isAssignableFrom(String.class)) {
                    preds[1] = this.builder.equal(criteriaRoot.get(field.getName()), entityId.toString());
                } else {
                    preds[1] = this.builder.equal(criteriaRoot.get(field.getName()), entityId);
                }

            }
        }
        try {
            c.where(preds);
            return (E) this.entityManager.createQuery(c).getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            log.warn("Changes not found for entity name {} and entityId {} returning current entity", entityId, this.entityClazz.getSimpleName());
            return null;
        }
    }

    public List<String> handleEditRequestChild(T entity, T oldEntity, Class<E> editEntity) throws
            IllegalAccessException, JsonProcessingException, ExpectationFailed {
        Serializable index = null;
        List<String> changes = new ArrayList<>();

        if (null != entity) {
            //Check if entity has been modified
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (field.isAnnotationPresent(Id.class)) {
                    index = (Serializable) field.get(entity);
                }
            }
            if (index == null) {
                log.warn("Failed to find id field on entity {} during handling edit request", entity);
            } else {
                updateChanges(index, entity, oldEntity, editEntity);
            }
        }
        return changes;
    }

    /**
     * Used by jackson mapper to  filter relational entities
     */
    @JsonFilter("dynamicFilter")
    class DynamicMixIn {
    }

}
