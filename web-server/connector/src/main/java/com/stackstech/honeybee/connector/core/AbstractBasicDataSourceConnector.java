package com.stackstech.honeybee.connector.core;

import com.stackstech.honeybee.connector.core.entity.PoolInfo;
import org.apache.commons.dbcp2.BasicDataSource;

public abstract class AbstractBasicDataSourceConnector extends AbstractRessourceSession implements ResourceConfig {

    protected BasicDataSource dataSource = null;

    @Override
    public void resetPoolSize(Integer poolSize) {
        dataSource.setMaxTotal(poolSize);
    }

    @Override
    public Integer getPoolSize() {
        return dataSource.getMaxTotal();
    }

    @Override
    public PoolInfo getPoolInfo() {
        PoolInfo poolInfo = new PoolInfo();
        poolInfo.setMaxTotal(dataSource.getMaxTotal());
        poolInfo.setMaxIdle(dataSource.getMaxIdle());
        poolInfo.setMinIdle(dataSource.getMinIdle());
        poolInfo.setNumIdle(dataSource.getNumIdle());
        poolInfo.setNumActive(dataSource.getNumActive());

        return poolInfo;
    }
}
