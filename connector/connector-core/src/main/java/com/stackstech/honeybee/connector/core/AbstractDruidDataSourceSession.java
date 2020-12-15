package com.stackstech.honeybee.connector.core;

import com.alibaba.druid.pool.DruidDataSource;
import com.stackstech.honeybee.connector.core.entity.PoolInfo;

public abstract class AbstractDruidDataSourceSession extends AbstractRessourceSession implements ResourceConfig {

    protected DruidDataSource dataSource = null;

    @Override
    public void resetPoolSize(Integer poolSize) {
        dataSource.setMaxActive(poolSize);
    }

    @Override
    public Integer getPoolSize() {
        return dataSource.getPoolingCount();
    }

    @Override
    public PoolInfo getPoolInfo() {
        PoolInfo poolInfo = new PoolInfo();
        poolInfo.setMaxTotal(dataSource.getMaxActive());
        poolInfo.setMaxIdle(dataSource.getMaxIdle());
        poolInfo.setMinIdle(dataSource.getMinIdle());
        poolInfo.setNumIdle(dataSource.getPoolingCount());
        poolInfo.setNumActive(dataSource.getActiveCount());

        return poolInfo;
    }

}
