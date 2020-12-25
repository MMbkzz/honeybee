package com.stackstech.honeybee.server.bees.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JsonUtil.class);

    public static String toJson(Object obj) throws JsonProcessingException {
        if (obj == null) {
            LOGGER.warn("Object cannot be empty!");
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static String toJsonWithFormat(Object obj)
            throws JsonProcessingException {
        if (obj == null) {
            LOGGER.warn("Object to be formatted cannot be empty!");
            return null;
        }
        ObjectWriter mapper = new ObjectMapper().writer()
                .withDefaultPrettyPrinter();
        return mapper.writeValueAsString(obj);
    }

    public static <T> T toEntity(String jsonStr, Class<T> type)
            throws IOException {
        if (StringUtils.isEmpty(jsonStr)) {
            LOGGER.warn("Json string {} is empty!", type);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return mapper.readValue(jsonStr, type);
    }

    public static <T> T toEntity(File file, TypeReference<T> type)
            throws IOException {
        if (file == null) {
            LOGGER.warn("File cannot be empty!");
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, type);
    }

    public static <T> T toEntity(InputStream in, TypeReference<T> type)
            throws IOException {
        if (in == null) {
            throw new NullPointerException("Input stream cannot be null.");
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(in, type);
    }

    public static <T> T toEntity(String jsonStr, TypeReference<T> type)
            throws IOException {
        if (StringUtils.isEmpty(jsonStr)) {
            LOGGER.warn("Json string {} is empty!", type);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, type);
    }

}
