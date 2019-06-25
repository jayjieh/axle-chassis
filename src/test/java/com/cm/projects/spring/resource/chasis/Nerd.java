/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cm.projects.spring.resource.chasis;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.NickName;
import ke.axle.chassis.annotations.Searchable;
import ke.axle.chassis.annotations.Unique;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Cornelius M
 */
@Entity
@NickName(name = "Developer")
public class Nerd implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Unique(fieldName = "Name")
    @Searchable
    private String name;
    @ModifiableField
    @Searchable
    private String expertise;
    private String action;
    @Filter
    private String actionStatus;
    private String intrash;
    @Filter
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    public Nerd() {
    }

    public Nerd(Long id) {
        this.id = id;
    }

    public Nerd(String name, String expertise) {
        this.name = name;
        this.expertise = expertise;
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Nerd{" + "id=" + id + ", name=" + name + ", expertise=" + expertise + ", action=" + action + ", actionStatus=" + actionStatus + ", intrash=" + intrash + '}';
    }

}
