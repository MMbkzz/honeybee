package com.stackstech.honeybee.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * Abstract data entity
 *
 * @param <E> Data entity type
 * @param <V> Data view type
 * @author William
 * @since 1.0
 */
@Data
public abstract class AbstractDataEntity<E, V> {

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
    abstract public E build(Long ownerId);

    /**
     * update entity object
     *
     * @param ownerId entity owner id
     * @return entity object
     */
    abstract public E update(Long ownerId);

    /**
     * build a entity object
     *
     * @param ownerId entity owner id
     * @return entity object
     */
    abstract public E build(Long ownerId, V vo);

    /**
     * update entity object
     *
     * @param ownerId entity owner id
     * @return entity object
     */
    abstract public E update(Long ownerId, V vo);

}
