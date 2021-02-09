package com.stackstech.honeybee.connector.mysql;

import java.util.Map;

public class MySQLConnectorFactory implements ResourceSessionFactory {

    private MySQLConnector mysqlConnector;

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
    public void close() {
        if (null == mysqlConnector) {
            return;
        }
        mysqlConnector.close();
    }

    @Override
    public ResourceSession getSession() {
        return mysqlConnector;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return this.config;
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
            return mysqlConnector = MySQLConnector.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    @Override
    public PoolInfo getPoolInfo() {
        return mysqlConnector.getPoolInfo();
    }
}
