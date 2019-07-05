# AXLE CHASSIS Generic Controller
This library is used to handle common create, write and delete actions on resources (APIs)

## Features
- Create new resource
- Update existing resource
- Delete existing resource
- Approve checker actions
- Decline checker actions
- Fetch, filter, search and paginate resources

### Create Resource
Used to persist new entities to the database. The following validations are carried out before an entity is persisted;
- Javax validations (@NotNull, @Size)
- Unique fields (Fields annotated with @Unique annotation)
- Validates relational entities 

##### Returns
ResponseEntity with statuses:
- 201 on success
- 409 on unique validation error
- 400 on validation error
- 404 for entities that don't exist

> If an id field is present on the entity it will be reset to null.

### Update Resource
Used to update entities by saving new changes to the edited record entity. For edited record to work The following annotation must be present on the relevant fields to be used to store changes;
- @EditEntity used to store the name of the entity being updated preferably should be a string For example
``` java
@EditEntity 
private String recordEntity;
```

- @EditDataWrapper used to store changes in JSON format preferably should be a String For example
``` java
@EditDataWrapper
private String data;
```

- @EditEntityId used to store entity id and you can use any data type that extends Serializable For example
``` java
@EditEntityId
private Long entityId;
```

- @EditChildEntity used to enable a child entity be updated while not updating the parent entity
``` java
@EditChildEntity
private Class ClassA{
 //class containing child entity
}
```
> For created and updated records that have not been approved the changes are persisted to the entity directly without being stored in the edited record entity

##### Returns
ResponseEntity with statuses:
- 200 on success
- 404 if the entity doesn't exist in the database
- 400 on validation errors (Relies on javax validation)
- 409 on unique field validation errors
- 417 if the entity us pending approval actions or if changes were not found on the current entity

##### Throws
- **IllegalAccessException** - If a field on the entity could not be accessed
- **com.fasterxml.jackson.core.JsonProcessingException** - If changes could not be converted to JSON string
- **ExpectationFailed** - When editEntity does not have fields; @EditEntity, @EditEntity and @EditEntityId

### Delete Resource
Used to delete existing resource
> If action and actionStatus fields don't exist the record is moved to trash directly (flagging . **If intrash field doesn't exist the record is deleted permanently**)

##### Returns
ResponseEntity with statuses:
- 200 on success
- 207 if not all records could be deleted
- 404 if the entity doesn't exist in the database

### Approve Actions
Used to approve actions (create, update, delete, deactivate). Also ensures only the checker can approve an action

##### Returns
ResponseEntity with statuses:
- 200 on success
- 404 if the entity doesn't exist in the database
- 207 if some of the action could be approved successfully. The data fields contains more details on records that failed

##### Throws
- **ExpectationFailed** - When entity doesn't have action or actionStatus fields

### Decline Actions
Used to decline actions (create, update, delete, deactivate). Ensures only the checker can decline an action

##### Returns
ResponseEntity with statuses:
- 200 on success
- 404 if the entity doesn't exist in the database
- 207 if some of the action could be approved successfully. The data fields contains more details on records that failed

##### Throws
- **ExpectationFailed** - When entity doesn't have action or actionStatus fields

### Fetch Resources
Used to retrieve all entity records
> - If needle parameter is present search will be done on fields annotated with @Searchable (Search is case insensitive)
> - If fields annotated with @Filter exist the request will be searched for parameters with similar name as the field name and if found the results will be filtered using the filter. For example for field `@Filter private String name;` expects the request name parameter to be name.
    To filter by date range you need to provide to and from request parameters with a valid String date (dd/MM/yyyy, dd/MM/yyyy HH:mm:ss.SSS, dd/MM/yyyy HH:mm:ss)

##### Throws
- **ParseException** - if request param date cannot be casted to **java.util.Date**

##### Adding the library dependency to your Projects
If your using maven
``` java
 <dependency>
      <groupId>io.github.jayjieh</groupId>
      <artifactId>SpringResourceChasis</artifactId>
      <version>1.2.6</version>
  </dependency>
  ```
  If your Using Gradle
``` java
  compile group: 'io.github.jayjieh', name: 'SpringResourceChasis', version: '1.2.6'
```

#### Extending it in your Controller 
``` java
@RestController
@RequestMapping(value = "/demo")
public class DemoController extends ChasisResource<Users, Long, Object> {

      public DemoController(LoggerService loggerService, EntityManager entityManager) {
           super(loggerService, entityManager);
      }
}
```
> - **Users**  - This is the example Entity Class mapping generated from your Database table
> - **Long**   - Represents the Datatype for @Id variable in your entity
> - **Object** - This can be replace with any entity that could be used manage Controllers update as described above in [**Update Resource**](https://github.com/jayjieh/axle-chassis#update-resource)



