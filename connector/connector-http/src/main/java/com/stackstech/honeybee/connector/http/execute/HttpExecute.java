package com.stackstech.honeybee.connector.http.execute;

import com.stackstech.dcp.connector.core.entity.DriverModel;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;

/**
 *
 */
public interface HttpExecute {

    Object execute(CloseableHttpClient httpclient, DriverModel driverModel, Map<String, Object> headParam, HttpHost proxy);
}
