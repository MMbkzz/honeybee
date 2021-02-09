package com.stackstech.honeybee.connector.postgresql;


import java.util.Map;

public class PostgreSQLConnectorFactory implements ResourceSessionFactory {

    private PostgreSQLConnector postgreSQLConnector;

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
        return postgreSQLConnector;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return this.config;
    }

    @Override
    public void close() {
        if (null == postgreSQLConnector) {
            return;
        }
        postgreSQLConnector.close();
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
        return postgreSQLConnector.getPoolInfo();
    }


    private ResourceSession openSessionFromDataSource(ClassLoader classLoader, Map<String, Object> config) {
        try {
            return postgreSQLConnector = PostgreSQLConnector.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}
