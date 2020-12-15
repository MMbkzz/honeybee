package com.stackstech.honeybee.connector.http.execute.impl;

import com.stackstech.dcp.connector.core.entity.DriverModel;
import com.stackstech.honeybee.connector.http.execute.HttpExecute;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class HttpGetExecute implements HttpExecute {
    @Override
    public Object execute(CloseableHttpClient httpClient, DriverModel driverModel, Map<String, Object> headParam, HttpHost proxy) {

        String url = driverModel.getExpression();
        HttpGet httpGet = new HttpGet(url);
        if (proxy != null) {
            httpGet.setConfig(RequestConfig.custom().setProxy(proxy).build());
        }

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());

                return strResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
