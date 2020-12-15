package com.stackstech.honeybee.connector.core;

import com.stackstech.honeybee.connector.core.entity.PoolInfo;
import org.apache.commons.pool2.impl.GenericObjectPool;

public abstract class AbstractPoolSession<T> extends AbstractRessourceSession implements ResourceConfig {

    public GenericObjectPool<T> pool;

    @Override
    public void resetPoolSize(Integer poolSize) {
        pool.setMaxTotal(poolSize);
    }

    @Override
    public Integer getPoolSize() {
        return pool.getMaxTotal();
    }

    @Override
    public PoolInfo getPoolInfo() {
        PoolInfo poolInfo = new PoolInfo();
        poolInfo.setMaxTotal(pool.getMaxTotal());
        poolInfo.setMaxIdle(pool.getMaxIdle());
        poolInfo.setMinIdle(pool.getMinIdle());
        poolInfo.setNumIdle(pool.getNumIdle());
        poolInfo.setNumActive(pool.getNumActive());

        return poolInfo;
    }
}
