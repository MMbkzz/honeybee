package com.stackstech.honeybee.connector.postgresql;


import com.stackstech.dcp.connector.core.ResourceSession;
import com.stackstech.dcp.connector.core.ResourceSessionFactory;
import com.stackstech.dcp.connector.core.entity.PoolInfo;

import java.util.Map;

public class PostgreSQLSessionFactory implements ResourceSessionFactory {

    private PostgreSQLSession postgreSQLSession;

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
        return postgreSQLSession;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return this.config;
    }

    @Override
    public void close() {
        if (null == postgreSQLSession) {
            return;
        }
        postgreSQLSession.close();
    }

    @Override
    public void resetPoolSize(Integer poolSize) {

        this.getSession().resetPoolSize(poolSize);
    }

    @Override
    public Integer getPoolSize() {
        return this.getSession().getPoolSize();
    }

    @Override
    public PoolInfo getPoolInfo() {
        return postgreSQLSession.getPoolInfo();
    }


    private ResourceSession openSessionFromDataSource(ClassLoader classLoader, Map<String, Object> config) {
        try {
            return postgreSQLSession = PostgreSQLSession.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}
