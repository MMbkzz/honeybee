package com.stackstech.dcp.connector.core.executor;

import com.stackstech.dcp.connector.core.DriverExecutor;
import com.stackstech.dcp.connector.core.ResourceSession;
import com.stackstech.dcp.connector.core.entity.DriverMetaData;
import com.stackstech.dcp.connector.core.entity.DriverModel;

public class DriverExecutorWrite implements DriverExecutor {

    @Override
    public DriverMetaData execute(ResourceSession resourceSession, DriverModel driverModel) {
        return resourceSession.put(driverModel);
    }
}
