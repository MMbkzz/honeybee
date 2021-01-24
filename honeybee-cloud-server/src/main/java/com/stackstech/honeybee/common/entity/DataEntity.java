package com.stackstech.honeybee.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * Abstract data entity
 *
 * @param <T> Data entity type
 * @author William
 * @since 1.0
 */
@Data
public abstract class DataEntity<T> {

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
     * build a entity object
     *
     * @param ownerId entity owner id
     * @return entity object
     */
    abstract public T build(Long ownerId);

    /**
     * update entity object
     *
     * @param ownerId entity owner id
     * @return entity object
     */
    abstract public T update(Long ownerId);

}
