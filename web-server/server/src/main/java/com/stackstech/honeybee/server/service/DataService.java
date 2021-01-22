package com.stackstech.honeybee.server.service;

import java.util.Map;

/**
 * Data operation service interface
 *
 * @param <T> Data entity class type
 * @author William
 * @since 1.0
 */
public interface DataService<T> {

    /**
     * Add data record
     *
     * @param vo      Data model
     * @param ownerId Owner ID
     * @return boolean
     */
    boolean add(T vo, Long ownerId);

    /**
     * Update data record
     *
     * @param vo      Data model
     * @param ownerId Owner ID
     * @return boolean
     */
    boolean update(T vo, Long ownerId);

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
    Object getSingle(Long recordId);


    /**
     * Get multiple data records
     *
     * @param parameter Query parameters
     * @return List of data result
     */
    Object get(Map<String, Object> parameter);

    /**
     * Get the number of multiple data records
     *
     * @param parameter Query parameters
     * @return Integer
     */
    Integer getTotalCount(Map<String, Object> parameter);
}
