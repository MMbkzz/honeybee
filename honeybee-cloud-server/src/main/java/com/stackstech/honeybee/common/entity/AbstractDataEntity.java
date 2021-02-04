package com.stackstech.honeybee.common.entity;

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
    protected Integer status;

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
     * entity status name
     */
    protected String statusName;

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
