package com.stackstech.honeybee.connector.core;

import com.stackstech.honeybee.connector.core.entity.PoolInfo;

public interface ResourceConfig {

    /**
     * 重置资源池大小
     *
     * @param poolSize
     */
    void resetPoolSize(Integer poolSize);

    /**
     * 获取资源池大小
     *
     * @return
     */
    Integer getPoolSize();

    /**
     * 获取资源池信息
     *
     * @return
     */
    PoolInfo getPoolInfo();

}
