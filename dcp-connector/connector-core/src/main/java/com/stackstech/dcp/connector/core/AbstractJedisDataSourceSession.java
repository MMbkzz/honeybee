package com.stackstech.dcp.connector.core;

import com.stackstech.dcp.connector.core.entity.PoolInfo;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public abstract class AbstractJedisDataSourceSession extends AbstractRessourceSession implements ResourceConfig {

    protected JedisPool dataSource = null;

    protected JedisPoolConfig dataSourceConfig = null;

    @Override
    public void resetPoolSize(Integer poolSize) {
        dataSourceConfig.setMaxTotal(poolSize);
    }

    @Override
    public Integer getPoolSize() {
        return dataSourceConfig.getMaxTotal();
    }

    @Override
    public PoolInfo getPoolInfo() {
        PoolInfo poolInfo = new PoolInfo();
        poolInfo.setMaxTotal(dataSourceConfig.getMaxTotal());
        poolInfo.setMaxIdle(dataSourceConfig.getMaxIdle());
        poolInfo.setMinIdle(dataSourceConfig.getMinIdle());
        poolInfo.setNumIdle(dataSource.getNumIdle());
        poolInfo.setNumActive(dataSource.getNumActive());

        return poolInfo;
    }
}
