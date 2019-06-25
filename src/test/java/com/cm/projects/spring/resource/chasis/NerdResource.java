/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cm.projects.spring.resource.chasis;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import javax.persistence.EntityManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Cornelius M
 */
@RestController
@RequestMapping(value = "/developer")
public class NerdResource extends ChasisResource<Nerd, Long, EdittedRecord> {

        public NerdResource(LoggerService loggerService, EntityManager entityManager) {
            super(loggerService, entityManager);
        }

    }
