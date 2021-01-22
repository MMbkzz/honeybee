package com.stackstech.honeybee.server.service;

import java.util.List;
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
     * @param entity  Data model
     * @param ownerId Owner ID
     * @return boolean
     */
    boolean add(T entity, Long ownerId);

    /**
     * Update data record
     *
     * @param entity  Data model
     * @param ownerId Owner ID
     * @return boolean
     */
    boolean update(T entity, Long ownerId);

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
     * @return Data entity
     */
    T getSingle(Long recordId);


    /**
     * Get multiple data records
     *
     * @param parameter Query parameters
     * @return List of Data entity
     */
    List<T> get(Map<String, Object> parameter);

    /**
     * Get the number of multiple data records
     *
     * @param parameter Query parameters
     * @return Integer
     */
    Integer getTotalCount(Map<String, Object> parameter);
}
