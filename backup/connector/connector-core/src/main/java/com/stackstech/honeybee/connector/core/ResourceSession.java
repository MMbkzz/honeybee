package com.stackstech.honeybee.connector.core;


import com.stackstech.honeybee.connector.core.entity.DriverMetaData;
import com.stackstech.honeybee.connector.core.entity.DriverModel;

import java.util.List;
import java.util.Map;

/**
 * 资源回话
 */
public interface ResourceSession extends ResourceConfig {

    /**
     * 查询资源
     */
    List<Map<String, Object>> get(String statement);


    DriverMetaData get(DriverModel driverModel);

    /**
     * 存放资源
     */
    DriverMetaData put(DriverModel driverModel);

    /**
     * 验证资源有效
     */
    boolean valid();

    /**
     * 关闭/释放资源
     */
    void close();

}
