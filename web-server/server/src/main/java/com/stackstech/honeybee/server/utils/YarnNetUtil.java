package com.stackstech.honeybee.server.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stackstech.honeybee.server.quality.entity.JobInstanceBean;
import com.stackstech.honeybee.server.quality.entity.LivySessionStates;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.stackstech.honeybee.server.quality.entity.LivySessionStates.State.DEAD;

public class YarnNetUtil {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(YarnNetUtil.class);
    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * delete app task scheduling by yarn.
     *
     * @param url   prefix part of whole url
     * @param appId application id
     */
    public static void delete(String url, String appId) {
        try {
            if (appId != null) {
                LOGGER.info("{} will delete by yarn", appId);
                restTemplate.put(url + "ws/v1/cluster/apps/"
                                + appId + "/state",
                        "{\"state\": \"KILLED\"}");
            }
        } catch (HttpClientErrorException e) {
            LOGGER.warn("client error {} from yarn: {}",
                    e.getMessage(), e.getResponseBodyAsString());
        } catch (Exception e) {
            LOGGER.error("delete exception happens by yarn. {}", e);
        }
    }

    /**
     * update app task scheduling by yarn.
     *
     * @param url      prefix part of whole url
     * @param instance job instance
     * @return
     */
    public static boolean update(String url, JobInstanceBean instance) {
        try {
            url += "/ws/v1/cluster/apps/" + instance.getAppId();
            String result = restTemplate.getForObject(url, String.class);
            JsonObject state = parse(result);
            if (state != null) {
                instance.setState(LivySessionStates.toLivyState(state));
            }
            return true;
        } catch (HttpClientErrorException e) {
            LOGGER.warn("client error {} from yarn: {}",
                    e.getMessage(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // in sync with Livy behavior, see com.cloudera.livy.utils.SparkYarnApp
                instance.setState(DEAD);
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("update exception happens by yarn. {}", e);
        }
        return false;
    }

    /**
     * parse json string and get app json object.
     *
     * @param json json string
     * @return
     */
    public static JsonObject parse(String json) {
        if (StringUtils.isEmpty(json)) {
            LOGGER.warn("Input string is empty.");
            return null;
        }
        JsonParser parser = new JsonParser();
        return parser.parse(json).getAsJsonObject().getAsJsonObject("app");
    }
}

