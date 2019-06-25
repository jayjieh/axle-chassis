/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cm.projects.spring.resource.chasis;

import ke.axle.chassis.annotations.EditDataWrapper;
import ke.axle.chassis.annotations.EditEntity;
import ke.axle.chassis.annotations.EditEntityId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Cornelius M
 */
@Entity
public class EdittedRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 50)
    @Column(name = "TAX_ENTITY")
    @EditEntity
    private String taxEntity;
    @Size(max = 4000)
    @Column(name = "\"VALUES\"")
    @EditDataWrapper
    private String values;
    @Column(name = "EDIT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editTime;
    @Size(max = 20)
    @Column(name = "ENTITY_ID")
    @EditEntityId
    private String entityId;

    public EdittedRecord() {
    }

    public EdittedRecord(BigDecimal id) {
        this.id = id;
    }
    
    public EdittedRecord(String taxEntity, String values, String entityId) {
        this.taxEntity = taxEntity;
        this.values = values;
        this.entityId = entityId;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getTaxEntity() {
        return taxEntity;
    }

    public void setTaxEntity(String taxEntity) {
        this.taxEntity = taxEntity;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EdittedRecord)) {
            return false;
        }
        EdittedRecord other = (EdittedRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.co.tracom.congo.etax.entities.TaxEdittedRecord[ id=" + id + " ]";
    }

}
