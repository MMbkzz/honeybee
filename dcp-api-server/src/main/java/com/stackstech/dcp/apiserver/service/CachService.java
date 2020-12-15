package com.stackstech.dcp.apiserver.service;

/**
 * 缓存服务
 */
public interface CachService {

    /**
     * 缓存数据
     *
     * @param dataServiceId
     * @param resutlValue
     */
    void put(String dataServiceId, String resutlValue);

    /**
     * 获取缓存数据
     *
     * @param dataServiceId
     * @return
     */
    Object get(String dataServiceId);

    /**
     * 删除缓存数据
     *
     * @param dataServiceId
     */
    void delete(String dataServiceId);
}
