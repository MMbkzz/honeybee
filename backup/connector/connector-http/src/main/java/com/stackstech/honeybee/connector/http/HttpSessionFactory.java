package com.stackstech.honeybee.connector.http;

import com.stackstech.honeybee.connector.core.ResourceSession;
import com.stackstech.honeybee.connector.core.ResourceSessionFactory;
import com.stackstech.honeybee.connector.core.entity.PoolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HttpSessionFactory implements ResourceSessionFactory {

    private static final Logger logger = LoggerFactory.getLogger(HttpSession.class);


    private HttpSession httpSession;

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
        return httpSession;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return this.config;
    }

    @Override
    public void close() {
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
            return httpSession = HttpSession.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public PoolInfo getPoolInfo() {
        return httpSession.getPoolInfo();
    }
}
