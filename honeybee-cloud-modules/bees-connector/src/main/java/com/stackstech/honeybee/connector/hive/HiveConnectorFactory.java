package com.stackstech.honeybee.connector.hive;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class HiveConnectorFactory implements ResourceSessionFactory {

    private static final Logger logger = LoggerFactory.getLogger(HiveConnector.class);

    private HiveConnector hiveConnector;

    private final Map<String, Object> config = Maps.newHashMap();

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
        return hiveConnector;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return this.config;
    }

    @Override
    public void close() {
        if (null == hiveConnector) {
            return;
        }
        hiveConnector.close();
    }

    @Override
    public void resetPoolSize(Integer poolSize) {

        this.getSession().resetPoolSize(poolSize);
    }

    @Override
    public Integer getPoolSize() {
        return this.getSession().getPoolSize();
    }

    private void openSessionFromDataSource(ClassLoader classLoader, Map<String, Object> config) {
        try {
            hiveConnector = HiveConnector.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public PoolInfo getPoolInfo() {
        return hiveConnector.getPoolInfo();
    }
}
