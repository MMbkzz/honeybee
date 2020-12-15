package com.stackstech.dcp.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class JacksonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
    private static ObjectMapper objectMapper = null;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }

    private JacksonUtil() {
    }

    /**
     * @param @param  obj
     * @param @return 参数说明
     * @return String    返回类型
     * @throws
     * @Title: beanToJson
     */
    public static String beanToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("beanToJson error:", e);
        }
        return null;
    }

    /**
     * @param @param  str
     * @param @param  clazz
     * @param @return 参数说明
     * @return T    返回类型
     * @throws
     * @Title: jsonToBean
     */
    public static <T> T jsonToBean(String str, Class<T> clazz) {
        try {
            return objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            logger.error("jsonToBean error:", e);
        }
        return null;
    }

    public static <T> T jsonToBean(JsonNode jsonNode, Class<T> clazz) {
        try {
            if (jsonNode != null && clazz != null) {
                return objectMapper.readValue(jsonNode.toString(), clazz);
            }
        } catch (IOException e) {
            logger.error("jsonToBean error:", e);
        }
        return null;
    }

    public static <T> T jsonToCollections(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        try {

            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            logger.error("jsonToBean error:", e);
        }
        return null;
    }

    public static JsonNode getJsonNode(String plain) {
        JsonNode root = null;
        try {
            root = objectMapper.readTree(plain);
        } catch (Exception e) {
            logger.error("getJsonNode error:", e);
        }
        return root;
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static Long getLong(JsonNode jsonNode, String nodeName) {
        if (jsonNode.has(nodeName)) {
            jsonNode = jsonNode.get(nodeName);
            if (!jsonNode.isNull()) {
                return jsonNode.asLong();
            }
        }
        return null;
    }

    public static Integer getInt(JsonNode jsonNode, String nodeName) {
        if (jsonNode.has(nodeName)) {
            jsonNode = jsonNode.get(nodeName);
            if (!jsonNode.isNull()) {
                return jsonNode.asInt();
            }
        }
        return null;
    }


    public static String getString(JsonNode jsonNode, String nodeName) {
        if (jsonNode.has(nodeName)) {
            jsonNode = jsonNode.get(nodeName);
            if (!jsonNode.isNull()) {
                return jsonNode.asText();
            }
        }
        return null;
    }

    public static Boolean getBoolean(JsonNode jsonNode, String nodeName) {
        if (jsonNode.has(nodeName)) {
            jsonNode = jsonNode.get(nodeName);
            if (!jsonNode.isNull()) {
                return jsonNode.asBoolean();
            }
        }
        return null;
    }

    /**
     * 将Map<String , Object>数据转为 String
     *
     * @param map
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @paramitems
     */
    public static String convertToJsonStrs(Map<String, Object> map) throws Exception {
        if (null == map || map.isEmpty()) {
            return "";
        }
        return objectMapper.writeValueAsString(map);
    }

    /**
     * 将json数据 转为Map<String , Object>
     *
     * @param jsonItems
     * @return
     */
    public static Map<String, Object> convertToMaps(String jsonItems) throws Exception {
        if ("".equals(jsonItems) || jsonItems == null) {
            return null;
        }
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
        };
        return objectMapper.readValue(jsonItems, typeReference);
    }

    /**
     * 将json数据 转为Map<String , Object>
     *
     * @param jsonItems
     * @return
     */

    public static <K, V> Map<K, V> jsonToMaps(String jsonItems, Class<? extends Map> mapClass,
                                              Class<?> kClass, Class<?> vClass) throws Exception {
        if ("".equals(jsonItems) || jsonItems == null) {
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(mapClass, kClass, vClass);
        return objectMapper.readValue(jsonItems, javaType);
    }

    /**
     * 将json数据 转为Map<String, List<String>>
     *
     * @param jsonItems
     * @return
     */
    public static Map<String, List<String>> convertToMapList(String jsonItems) throws Exception {
        if ("".equals(jsonItems) || jsonItems == null) {
            return null;
        }
        TypeReference<Map<String, List<String>>> typeReference = new TypeReference<Map<String, List<String>>>() {
        };
        return objectMapper.readValue(jsonItems, typeReference);
    }

    /**
     * 将json数据 List<String>
     *
     * @param jsonItems
     * @return
     */
    public static List<String> convertToList(String jsonItems) throws Exception {
        if ("".equals(jsonItems) || jsonItems == null) {
            return null;
        }
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {
        };
        return objectMapper.readValue(jsonItems, typeReference);
    }

    /**
     * bean转map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> beanToMaps(Object obj) throws Exception {
        if ("".equals(obj) || obj == null) {
            return null;
        }
        String beanStr = objectMapper.writeValueAsString(obj);
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> result = objectMapper.readValue(beanStr, typeReference);
        return result;
    }

    /**
     * @param json  准备转换json
     * @param clazz 集合元素类型
     * @return
     * @description json字符串转换成对象集合
     * @author paul
     * @date 2017年8月12日 下午1:28:27
     * @update 2017年8月12日 下午1:28:27
     * @version V1.0
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> parseJsonList(String json, Class<T> clazz) {
        try {
            JavaType javaType = getCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param collectionClass 集合类
     * @param elementClasses  集合元素类
     * @return
     * @description 获取泛型的ColloectionType
     * @author paul
     * @date 2017年8月12日 下午2:17:38
     * @update 2017年8月12日 下午2:17:38
     * @version V1.0
     */
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}
