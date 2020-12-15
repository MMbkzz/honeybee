package com.stackstech.honeybee.connector.hbase;

import com.google.common.collect.Maps;
import com.stackstech.dcp.connector.core.ResourceSession;
import com.stackstech.dcp.connector.core.ResourceSessionFactory;
import com.stackstech.dcp.connector.core.entity.PoolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class HbaseSessionFactory implements ResourceSessionFactory {

    private static final Logger logger = LoggerFactory.getLogger(HbaseSessionFactory.class);


    private HbaseSession hbaseSession;

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
        return hbaseSession;
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return this.config;
    }

    @Override
    public void close() {
        if (null == hbaseSession) {
            return;
        }
        hbaseSession.close();
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
        return hbaseSession.getPoolInfo();
    }


    private void openSessionFromDataSource(ClassLoader classLoader, Map<String, Object> config) {
        try {
            hbaseSession = HbaseSession.getInstance(classLoader).initialize(config);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
