package com.stackstech.honeybee.connector.http.execute;

import com.stackstech.dcp.connector.core.entity.DriverModel;
import com.stackstech.honeybee.connector.http.execute.impl.HttpGetExecute;
import com.stackstech.honeybee.connector.http.execute.impl.HttpMapPostExecute;
import com.stackstech.honeybee.connector.http.execute.impl.HttpPostExecute;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;

/**
 *
 */
public class HttpAdapter {
    HttpExecute httpExecute = null;

    public HttpAdapter(String type) {

        if ("get".equals(type)) {
            httpExecute = new HttpGetExecute();
        } else if ("post".equals(type)) {
            httpExecute = new HttpPostExecute();
        } else if ("mappost".equals(type)) {
            httpExecute = new HttpMapPostExecute();
        }
    }

    public Object execute(CloseableHttpClient httpclient, DriverModel driverModel, Map<String, Object> headParam, HttpHost proxy) {
        return httpExecute.execute(httpclient, driverModel, headParam, proxy);
    }
}

