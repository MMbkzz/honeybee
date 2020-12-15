package com.stackstech.honeybee.apiserver.service;

import java.util.Map;

public interface ResourceService {

    /**
     * 获取本地资源池大小
     *
     * @return
     */
    Map<String, Integer> getLocalResource();

    /**
     * 获取预期资源池大小
     *
     * @return
     */
    Map<String, Integer> getExpectResource();

    /**
     * 保存本地资源池信息
     *
     * @param resourceId
     * @param poolSize
     */
    void saveHoldResource(String resourceId, Integer poolSize);

    /**
     * 保存本地资源池信息
     *
     * @param resource
     */
    void saveHoldResource(Map<String, Integer> resource);

    /**
     * 重置资源池大小
     *
     * @param resourceId
     * @param poolSize
     */
    void resetPoolSize(String resourceId, Integer poolSize);

    /**
     * 初始化资源
     *
     * @param resourceId
     */
    void initResource(String resourceId);

    /**
     * 删除资源
     *
     * @param resourceId
     */
    void removeResource(String resourceId);

    /**
     * 当前节点资源配置
     */
    void configResource();

    /**
     * 当前节点资源配置
     */
    void configExpectResource(String resourceId, Integer expectPoolSize, Map<String, Integer> hold);

    /**
     * 保存本地活跃资源信息
     *
     * @param resourceId
     * @param poolSize
     */
    void saveActiveResource(String resourceId, Integer poolSize);

}
