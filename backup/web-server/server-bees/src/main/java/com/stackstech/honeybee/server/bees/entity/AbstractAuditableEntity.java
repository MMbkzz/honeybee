package com.stackstech.honeybee.server.bees.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * AbstractAuditableEntity is base entity class definition in Apache Griffin, all
 * {@link javax.persistence.Entity} classes should extend it.
 */
@MappedSuperclass
public abstract class AbstractAuditableEntity implements Serializable {

    private static final long serialVersionUID = 4161638281338218249L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @JsonIgnore
    private Long createdDate = System.currentTimeMillis();

    @JsonIgnore
    private Timestamp modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractAuditableEntity other = (AbstractAuditableEntity) obj;
        if (id == null) {
            return other.id == null;
        } else {
            return id.equals(other.id);
        }
    }

}
