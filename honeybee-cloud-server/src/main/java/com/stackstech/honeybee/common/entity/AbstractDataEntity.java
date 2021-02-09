package com.stackstech.honeybee.common.entity;

import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import lombok.Data;

import java.util.Date;

/**
 * Abstract data entity
 *
 * @param <E> Data entity type
 * @author William
 * @since 1.0
 */
@Data
public abstract class AbstractDataEntity<E> {

    /**
     * entity status
     */
    protected EntityStatusType status;

    /**
     * entity status name
     */
    protected String statusName;

    /**
     * entity owner id
     */
    protected Long owner;

    /**
     * entity last update time
     */
    protected Date updatetime;

    /**
     * entity create time
     */
    protected Date createtime;

    /**
     * get entity status name
     */
    public String getStatusName() {
        return status.getName();
    }

    /**
     * build a entity object
     *
     * @param ownerId entity owner id
     * @return entity object
     */
    abstract public E build(Long ownerId);

    /**
     * update entity object
     *
     * @param ownerId entity owner id
     * @return entity object
     */
    abstract public E update(Long ownerId);

    /**
     * copy properties from view object
     *
     * @param vo view object
     * @return entity object
     */
    abstract public E copy(Object vo);

}
