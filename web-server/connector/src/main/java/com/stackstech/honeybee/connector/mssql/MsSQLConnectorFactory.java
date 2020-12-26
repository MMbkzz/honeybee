package com.stackstech.honeybee.connector.mssql;
 
import com.stackstech.honeybee.connector.core.ResourceSession;
import com.stackstech.honeybee.connector.core.ResourceSessionFactory;
import com.stackstech.honeybee.connector.core.entity.PoolInfo;

import java.util.Map;


public class MsSQLConnectorFactory implements ResourceSessionFactory {

    private MsSQLConnector msSQLConnector;

    private final Map<String, Object> config = null;

    @Override
    public ResourceSessionFactory openSession(ClassLoader classLoader, Map<String, Object> config) {
        openSessionFromDataSource(classLoader, config);
        return this;
    }

    @Override
    public ResourceSessionFactory reconfigure(ClassLoader classLoader, Map<String, Object> config) {
        openSessionFromDataSource(classLoader, config);
        return this;
    }

    @Override
    public ResourceSession getSession() {
        return msSQLConnector;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return this.config;
    }

    @Override
    public void close() {
        if (null == msSQLConnector) {
            return;
        }
        msSQLConnector.close();
    }

    @Override
    public void resetPoolSize(Integer poolSize) {

        this.getSession().resetPoolSize(poolSize);
    }

    @Override
    public Integer getPoolSize() {
        return this.getSession().getPoolSize();
    }

    private ResourceSession openSessionFromDataSource(ClassLoader classLoader, Map<String, Object> config) {
        try {
            return msSQLConnector = MsSQLConnector.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    @Override
    public PoolInfo getPoolInfo() {
        return msSQLConnector.getPoolInfo();
    }
}
