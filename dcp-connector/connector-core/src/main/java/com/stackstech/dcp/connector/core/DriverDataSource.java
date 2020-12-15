package com.stackstech.dcp.connector.core;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverDataSource {

    private static final Logger logger = LoggerFactory.getLogger(DriverDataSource.class);

    public final int MAX_TOTAL = 8;
    public final int MAX_IDLE = 8;
    public final int MIN_IDLE = 0;

    private GenericObjectPool<Object> connectionPool;

    public DriverDataSource() {
        connectionPool = new GenericObjectPool<Object>(new DriverPoolFactory());
    }

    public void setMaxTotal(int maxTotal) {
        connectionPool.setMaxTotal(maxTotal);
    }

    public int getMaxIdle() {
        return connectionPool.getMaxIdle();
    }

    public void setMaxIdle(int maxIdle) {
        connectionPool.setMaxIdle(maxIdle);
    }

    public int getMinIdle() {
        return connectionPool.getMinIdle();
    }

    public void setMinIdle(int minIdle) {
        connectionPool.setMinIdle(minIdle);
    }

    public int getNumActive() {
        return connectionPool.getNumActive();
    }


    public GenericObjectPool<Object> getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(GenericObjectPool<Object> connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void close() {
        connectionPool.close();
    }

    public void check() {
        Object o = null;
        try {
            o = connectionPool.borrowObject();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                connectionPool.returnObject(o);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
