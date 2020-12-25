package com.stackstech.honeybee.connector.core;


import com.stackstech.honeybee.connector.core.entity.DriverMetaData;
import com.stackstech.honeybee.connector.core.entity.DriverModel;

public interface DriverExecutor {

    DriverMetaData execute(ResourceSession resourceSession, DriverModel driverModel);

}