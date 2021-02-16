package com.stackstech.honeybee.server.core.service;

import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;

import java.util.List;
import java.util.Map;

/**
 * Data operation service interface
 *
 * @param <E> Data entity class type
 * @author William
 * @since 1.0
 */
public interface BaseDataService<E> {

    /**
     * Add data record
     *
     * @param entity Data entity
     * @return boolean
     */
    boolean add(E entity) throws ServerException, DataNotFoundException;

    /**
     * Update data record
     *
     * @param entity Data entity
     * @return boolean
     */
    boolean update(E entity) throws ServerException, DataNotFoundException;

    /**
     * Delete data record
     *
     * @param recordId Data record ID
     * @param ownerId  Owner ID
     * @return boolean
     */
    boolean delete(Long recordId, Long ownerId) throws ServerException, DataNotFoundException;

    /**
     * Get a single data record
     *
     * @param recordId Data record ID
     * @return Data result
     */
    E getSingle(Long recordId) throws ServerException, DataNotFoundException;


    /**
     * Get multiple data records
     *
     * @param parameter Query parameters
     * @return List of data result
     */
    List<E> get(Map<String, Object> parameter) throws ServerException, DataNotFoundException;

    /**
     * Get the number of multiple data records
     *
     * @param parameter Query parameters
     * @return Integer
     */
    Integer getTotalCount(Map<String, Object> parameter) throws ServerException;
}
