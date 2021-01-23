package com.stackstech.honeybee.connector.core.executor;

import com.stackstech.honeybee.connector.core.DriverExecutor;
import com.stackstech.honeybee.connector.core.ResourceSession;
import com.stackstech.honeybee.connector.core.entity.DriverMetaData;
import com.stackstech.honeybee.connector.core.entity.DriverModel;

public class DriverExecutorAdapter {

    private DriverExecutor executor = null;

    public DriverExecutorAdapter(String runMode) {

        if ("read".equals(runMode)) {
            executor = new DriverExecutorRead();
        } else if ("write".equals(runMode)) {
            executor = new DriverExecutorWrite();
        }
    }

    public DriverMetaData execute(ResourceSession resourceSession, DriverModel driverModel) {
        return executor.execute(resourceSession, driverModel);
    }
}
