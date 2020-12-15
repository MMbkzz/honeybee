package com.stackstech.dcp.connector.redis;

import com.stackstech.dcp.connector.core.ResourceSession;
import com.stackstech.dcp.connector.core.ResourceSessionFactory;
import com.stackstech.dcp.connector.core.entity.PoolInfo;

import java.util.Map;

/**
 *
 */
public class RedisSessionFactory implements ResourceSessionFactory {

    private RedisSession redisSession;

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
        return redisSession;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return this.config;
    }

    @Override
    public void close() {
        if (null == redisSession) {
            return;
        }
        redisSession.close();
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
            return redisSession = RedisSession.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    @Override
    public PoolInfo getPoolInfo() {
        return redisSession.getPoolInfo();
    }
}
