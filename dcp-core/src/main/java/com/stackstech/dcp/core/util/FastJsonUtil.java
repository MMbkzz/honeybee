package com.stackstech.dcp.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class FastJsonUtil {

    /**
     * 校验JSON格式
     *
     * @param content
     * @return
     */
    public static boolean isJSON(String content) {
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 递归读取所有的key
     *
     * @param jsonObject
     */
    public static StringBuffer getAllKey(JSONObject jsonObject) {
        StringBuffer stringBuffer = new StringBuffer();
        jsonObject.keySet().forEach(key -> {
            stringBuffer.append(key).append(",");
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject innerObject = (JSONObject) jsonObject.get(key);
                stringBuffer.append(getAllKey(innerObject));
            } else if (jsonObject.get(key) instanceof JSONArray) {
                JSONArray innerObject = (JSONArray) jsonObject.get(key);
                stringBuffer.append(getAllKey(innerObject));
            }
        });
        return stringBuffer;
    }

    /**
     * 递归获取所有key
     *
     * @param json1
     * @return
     */
    public static StringBuffer getAllKey(JSONArray json1) {
        StringBuffer stringBuffer = new StringBuffer();
        if (json1 != null) {
            json1.forEach(key -> {
                if (key instanceof JSONObject) {
                    JSONObject innerObject = (JSONObject) key;
                    stringBuffer.append(getAllKey(innerObject));
                } else if (key instanceof JSONArray) {
                    JSONArray innerObject = (JSONArray) key;
                    stringBuffer.append(getAllKey(innerObject));
                }
            });
        }
        return stringBuffer;
    }

}