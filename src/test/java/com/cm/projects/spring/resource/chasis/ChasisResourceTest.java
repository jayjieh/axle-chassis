/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cm.projects.spring.resource.chasis;

import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Cornelius M
 * @version 0.0.1
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@SpringBootTest
public class ChasisResourceTest {

    @PersistenceContext
    EntityManager em;
    private NerdResource nerdResource;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public ChasisResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.nerdResource = new NerdResource(new LoggerServiceImpl(), em);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of create method, of class ChasisResource. Tests include:
     * <ul>
     * <li>Creating new resource successfully</li>
     * <li>Testing unique field validation</li>
     * </ul>
     */
    @Test
    @Rollback(false)//for other tests to use data inserted here
    public void testCreate() {
        log.info("=============== Running test on create resource ===============");
        //Test creating a resource with non existing relational fields
//        john.setGender(new Gender(Short.valueOf((short)1)));
        Nerd john = new Nerd("Cornelius M", "Developer");
        ResponseEntity<ResponseWrapper<Nerd>> res = this.nerdResource.create(john);
        assertEquals("Creating new entity failed", HttpStatus.CREATED, res.getStatusCode());
        
        log.info("Testing unique fields");
        Nerd mary = new Nerd("Cornelius M", "Developer");
        res = this.nerdResource.create(mary);
        assertEquals("Unique fields validation failed", HttpStatus.CONFLICT, res.getStatusCode());
        
        log.warn("Yet to implement tests for validating relational entities");
    }

    /**
     * Test of getEntity method, of class ChasisResource.
     */
    @Test
    public void testGetEntity() {
        log.info("=============== Testing fetching resource ===============");
        ResponseEntity<ResponseWrapper<Nerd>> res = this.nerdResource.getEntity(1L);
        log.debug("Get entity resulted to: {}", res.getBody());
//        assertEquals("Failed to retrieve existing resource", new Nerd(1L), res.getBody().getData());
    }
//
//    /**
//     * Test of updateEntity method, of class ChasisResource.
//     */
//    @Test
//    public void testUpdateEntity() throws Exception {
//        System.out.println("updateEntity");
//        Object t = null;
//        ChasisResource instance = null;
//        ResponseEntity<ResponseWrapper<T>> expResult = null;
//        ResponseEntity<ResponseWrapper<T>> result = instance.updateEntity(t);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteEntity method, of class ChasisResource.
//     */
//    @Test
//    public void testDeleteEntity() {
//        System.out.println("deleteEntity");
//        ChasisResource instance = null;
//        ResponseEntity<ResponseWrapper> expResult = null;
//        ResponseEntity<ResponseWrapper> result = instance.deleteEntity(null);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of approveActions method, of class ChasisResource.
//     */
//    @Test
//    public void testApproveActions() throws Exception {
//        System.out.println("approveActions");
//        ChasisResource instance = null;
//        ResponseEntity<ResponseWrapper> expResult = null;
//        ResponseEntity<ResponseWrapper> result = instance.approveActions(null);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of declineActions method, of class ChasisResource.
//     */
//    @Test
//    public void testDeclineActions() {
//        System.out.println("declineActions");
//        ChasisResource instance = null;
//        ResponseEntity<ResponseWrapper> expResult = null;
//        ResponseEntity<ResponseWrapper> result = instance.declineActions(null);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    /**
//     * Test of fetchChanges method, of class ChasisResource.
//     */
//    @Test
//    public void testFetchChanges() throws Exception {
//        System.out.println("fetchChanges");
//        Object id = null;
//        ChasisResource instance = null;
//        ResponseEntity<ResponseWrapper<List<String>>> expResult = null;
//        ResponseEntity<ResponseWrapper<List<String>>> result = instance.fetchChanges(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findAll method, of class ChasisResource.
//     */
//    @Test
//    public void testFindAll() {
//        System.out.println("findAll");
//        Pageable pg = null;
//        HttpServletRequest request = null;
//        ChasisResource instance = null;
//        ResponseEntity<ResponseWrapper<Page<T>>> expResult = null;
//        ResponseEntity<ResponseWrapper<Page<T>>> result = instance.findAll(pg, request);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    

    /**
     * Used to provide spring boot tests configurations
     */
    @SpringBootApplication
    static class TestConfiguration {
    }

    /**
     * Used to mock logger service
     */
    private class LoggerServiceImpl implements LoggerService {

        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public void log(String description, String entity, Object entityId, String activity, String activityStatus, String notes) {
            this.logger.info("\n========== TEST AUDIT LOG ========== \nentity: {} \nentityId: {} \nactivity: {} "
                    + "\nactivityStatus: {} \ndescription: {} \nnotes: {}", entity, 
                    entityId, activity, activityStatus, description, notes);
        }

        @Override
        public boolean isInitiator(String Entity, Object entityId, String activity) {
            return true; //as this is only a test
        }

        @Override
        public void log(String description, String entity, Object entityId, Long userId, String activity, String activityStatus, String notes) {
            this.logger.info("\n========== TEST AUDIT LOG ========== \nentity: {} \nentityId: {} \nactivity: {} "
                    + "\nactivityStatus: {} \ndescription: {} \nnotes: {}", entity, 
                    entityId, activity, activityStatus, description, notes);
        }

    }

}
