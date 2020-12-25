package com.stackstech.honeybee.connector.http.execute.impl;

import com.stackstech.honeybee.connector.core.entity.DriverModel;
import com.stackstech.honeybee.connector.http.execute.HttpExecute;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * post请求(用于key-value格式的参数)
 */
public class HttpMapPostExecute implements HttpExecute {
    @Override
    public Object execute(CloseableHttpClient httpclient, DriverModel driverModel, Map<String, Object> headParam, HttpHost proxy) {
        String url = driverModel.getExpression();
        Map params = (Map) driverModel.getRequestData();
        BufferedReader in = null;
        try {
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));
            for (String key : headParam.keySet()) {
                request.setHeader(key, (String) headParam.get(key));
            }
//            request.setHeader("Accept", "application/json");
//            request.setHeader("Content-Type", "application/json");
            //String charSet = "UTF-8";
            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));

            }
            request.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8.toString()));

            HttpResponse response = httpclient.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {    //请求成功
                in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent(), StandardCharsets.UTF_8));
                StringBuffer sb = new StringBuffer();
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }

                in.close();

                return sb.toString();
            } else {    //
                System.out.println("状态码：" + code);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

    }
}
