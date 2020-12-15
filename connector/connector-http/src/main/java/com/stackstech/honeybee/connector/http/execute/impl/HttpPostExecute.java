package com.stackstech.honeybee.connector.http.execute.impl;

import com.stackstech.honeybee.connector.core.entity.DriverModel;
import com.stackstech.honeybee.connector.http.execute.HttpExecute;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * post请求（用于请求json格式的参数）
 */
public class HttpPostExecute implements HttpExecute {
    @Override
    public Object execute(CloseableHttpClient httpclient, DriverModel driverModel, Map<String, Object> headParam, HttpHost proxy) {

        String url = driverModel.getExpression();
        List<Map<String, Object>> listValues = (List<Map<String, Object>>) driverModel.getRequestData();

        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        StringEntity entity = null;
        for (String key : headParam.keySet()) { //用驱动默认的head  httpPost.setHeader("Content-Type", "application/json");httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader(key, (String) headParam.get(key));
        }

        for (Map<String, Object> mapField : listValues) {
            if ("head".equals(mapField.get("field").toString().toLowerCase())) {
                httpPost.setHeader((String) mapField.get("name"), (String) mapField.get("value"));
            } else if ("body".equals(mapField.get("field").toString().toLowerCase())) {
                String charSet = "UTF-8";
                entity = new StringEntity((String) mapField.get("value"), charSet);
            }
        }

        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;

        if (proxy != null) {
            httpPost.setConfig(RequestConfig.custom().setProxy(proxy).build());
        }
        try {

            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            System.out.println(response.toString() + "&&&&&&&&&&&" + state);
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            } else {

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
