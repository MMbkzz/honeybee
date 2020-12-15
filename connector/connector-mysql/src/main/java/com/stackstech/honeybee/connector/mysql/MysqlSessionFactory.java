package com.stackstech.honeybee.connector.mysql;

import com.stackstech.honeybee.connector.core.ResourceSession;
import com.stackstech.honeybee.connector.core.ResourceSessionFactory;
import com.stackstech.honeybee.connector.core.entity.PoolInfo;

import java.util.Map;

public class MysqlSessionFactory implements ResourceSessionFactory {

    private MysqlSession mysqlSession;

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
        if (null == mysqlSession) {
            return;
        }
        mysqlSession.close();
    }

    @Override
    public ResourceSession getSession() {
        return mysqlSession;
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
            return mysqlSession = MysqlSession.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    @Override
    public PoolInfo getPoolInfo() {
        return mysqlSession.getPoolInfo();
    }
}
