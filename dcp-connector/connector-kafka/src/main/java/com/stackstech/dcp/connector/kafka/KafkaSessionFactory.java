package com.stackstech.dcp.connector.kafka;


import com.stackstech.dcp.connector.core.ResourceSession;
import com.stackstech.dcp.connector.core.ResourceSessionFactory;
import com.stackstech.dcp.connector.core.entity.PoolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KafkaSessionFactory implements ResourceSessionFactory {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSessionFactory.class);

    private KafkaSession kafkaSession;

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
        return kafkaSession;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return this.config;
    }

    @Override
    public void close() {
        if (null == kafkaSession) {
            return;
        }
        kafkaSession.close();
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
            return kafkaSession = KafkaSession.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public PoolInfo getPoolInfo() {
        return kafkaSession.getPoolInfo();
    }
}
