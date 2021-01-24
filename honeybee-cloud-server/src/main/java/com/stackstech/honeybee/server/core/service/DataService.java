package com.stackstech.honeybee.server.core.service;

import java.util.List;
import java.util.Map;

/**
 * Data operation service interface
 *
 * @param <E> Data entity class type
 * @author William
 * @since 1.0
 */
public interface DataService<E> {

    /**
     * Add data record
     *
     * @param entity Data entity
     * @return boolean
     */
    boolean add(E entity);

    /**
     * Update data record
     *
     * @param entity Data entity
     * @return boolean
     */
    boolean update(E entity);

    /**
     * Delete data record
     *
     * @param recordId Data record ID
     * @param ownerId  Owner ID
     * @return boolean
     */
    boolean delete(Long recordId, Long ownerId);

    /**
     * Get a single data record
     *
     * @param recordId Data record ID
     * @return Data result
     */
    E getSingle(Long recordId);


    /**
     * Get multiple data records
     *
     * @param parameter Query parameters
     * @return List of data result
     */
    List<E> get(Map<String, Object> parameter);

    /**
     * Get the number of multiple data records
     *
     * @param parameter Query parameters
     * @return Integer
     */
    Integer getTotalCount(Map<String, Object> parameter);
}
