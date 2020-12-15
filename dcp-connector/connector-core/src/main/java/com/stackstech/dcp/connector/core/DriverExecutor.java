package com.stackstech.dcp.connector.core;


import com.stackstech.dcp.connector.core.entity.DriverMetaData;
import com.stackstech.dcp.connector.core.entity.DriverModel;

public interface DriverExecutor {

    DriverMetaData execute(ResourceSession resourceSession, DriverModel driverModel);

}