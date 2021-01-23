package com.stackstech.honeybee.connector.core.executor;

import com.stackstech.honeybee.connector.core.DriverExecutor;
import com.stackstech.honeybee.connector.core.ResourceSession;
import com.stackstech.honeybee.connector.core.entity.DriverMetaData;
import com.stackstech.honeybee.connector.core.entity.DriverModel;

public class DriverExecutorRead implements DriverExecutor {

    @Override
    public DriverMetaData execute(ResourceSession resourceSession, DriverModel driverModel) {
        return resourceSession.get(driverModel);
    }
}
