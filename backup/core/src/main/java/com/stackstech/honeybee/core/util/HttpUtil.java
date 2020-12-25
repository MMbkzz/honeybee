package com.stackstech.honeybee.core.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Http连接工具类
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final String EXPECTED_TYPE_JSON = "application/json";
    private static final String EXPECTED_TYPE_TEXT = "text/plain";

    private static Client client = null;

    static {
        if (client == null) {
            client = Client.create();
        }
    }

    /**
     * Post请求访问<只需要带type,不需要accept></>
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, String params) {
        ClientResponse response = null;
        String responseString = null;
        try {
            WebResource webResource = client.resource(url);
            if (webResource != null) {
                response = webResource.header("token", headers.get("token")).header("appid", headers.get("appid"))
                        .header("data-type", headers.get("data-type"))
                        .accept(EXPECTED_TYPE_JSON).type(EXPECTED_TYPE_JSON)
                        .post(ClientResponse.class, params);
                if (response != null && response.getStatus() == 200) {
                    responseString = response.getEntity(String.class);
                }
            }
        } catch (Exception e) {
            logger.error("Post-Http request failed!", e);
            e.printStackTrace();
        } finally {
            closeResponse(response);
        }
        return responseString;
    }

    /**
     * Get请求访问
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, Object> params) {
        ClientResponse response = null;
        String responseString = null;
        try {
            MultivaluedMapImpl queryParams = new MultivaluedMapImpl();

            //api-server调用
            if (params.get("dataServiceId") != null) {
                getMap(params, headers, queryParams);
            } else {
                //common rest-api调用
                if (params != null && !params.isEmpty()) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        queryParams.add(entry.getKey(), entry.getValue());
                    }
                }
            }

            WebResource webResource = client.resource(url).queryParams(queryParams);
            if (webResource != null) {
                response = webResource.get(ClientResponse.class);
                if (response != null && response.getStatus() == 200) {
                    responseString = response.getEntity(String.class);
                }
            }
        } catch (Exception e) {
            logger.error("GET-Http request failed!", e);
            e.printStackTrace();
        } finally {
            closeResponse(response);
        }
        return responseString;
    }

    /**
     * 封装api-server请求参数
     *
     * @param params
     * @param queryParams
     */
    private static void getMap(Map<String, Object> params, Map<String, String> headers, MultivaluedMapImpl queryParams) {
        if (params.get("dataServiceId") != null) {
            queryParams.add("dataServiceId", (String) params.get("dataServiceId"));
        }
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                queryParams.add(entry.getKey(), entry.getValue());
            }
        }
        if (params.get("data") != null) {
            Map<String, Object> data = (Map<String, Object>) params.get("data");
            if (data != null) {
                List<Map<String, Object>> paramList = (List<Map<String, Object>>) data.get("params");
                if (paramList != null && paramList.size() > 0) {
                    for (Map<String, Object> map : paramList) {
                        queryParams.add((String) map.get("name"), (String) map.get("value"));
                    }
                }
            }
        }
    }

    /**
     * 关闭Response
     *
     * @param response
     */
    private static void closeResponse(ClientResponse response) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response = null;
    }

}
