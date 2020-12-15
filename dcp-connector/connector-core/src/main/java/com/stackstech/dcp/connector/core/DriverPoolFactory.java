package com.stackstech.dcp.connector.core;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class DriverPoolFactory extends BasePooledObjectFactory<Object> {


    @Override
    public Object create() {
        return new Object();
    }

    /**
     * Use the default PooledObject implementation.
     */
    @Override
    public PooledObject<Object> wrap(Object buffer) {
        return new DefaultPooledObject<Object>(buffer);
    }

    /**
     * When an object is returned to the pool, clear the buffer.
     */
    @Override
    public void passivateObject(PooledObject<Object> pooledObject) {
        pooledObject.getObject();
    }


}
