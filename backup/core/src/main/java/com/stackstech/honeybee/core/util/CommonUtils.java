package com.stackstech.honeybee.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public static List<Map<String, Object>> elementToArrays(List<?> lists) {
        List<Map<String, Object>> converts = Lists.newArrayList();
        if (lists != null && lists.size() > 0) {
            lists.forEach(o -> {
                try {
                    converts.add(elementToMap(o));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            });
            return converts;
        }
        return null;
    }

    public static Map<String, Object> elementToMap(Object o) {
        assert o != null;
        String json = JSON.toJSONString(o);
        return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        });
    }
}
