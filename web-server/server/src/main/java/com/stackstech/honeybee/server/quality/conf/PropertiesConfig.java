package com.stackstech.honeybee.server.quality.conf;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static com.stackstech.honeybee.server.quality.conf.EnvConfig.getBatchEnv;
import static com.stackstech.honeybee.server.quality.conf.EnvConfig.getStreamingEnv;
import static com.stackstech.honeybee.server.utils.JsonUtil.toEntity;
import static com.stackstech.honeybee.server.utils.PropertiesUtil.getConf;
import static com.stackstech.honeybee.server.utils.PropertiesUtil.getConfPath;

/**
 * PropertiesConfig is responsible for initializing configuration objects
 * from property files.
 */
@Configuration
public class PropertiesConfig {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PropertiesConfig.class);

    public static Map<String, Object> livyConfMap;

    private String configLocation;

    private String envLocation;

    public PropertiesConfig(
            @Value("${external.config.location}") String configLocation,
            @Value("${external.env.location}") String envLocation) {
        LOGGER.info("external.config.location : {}",
                configLocation != null ? configLocation : "null");
        LOGGER.info("external.env.location : {}",
                envLocation != null ? envLocation : "null");
        this.configLocation = configLocation;
        this.envLocation = envLocation;
    }

    @PostConstruct
    public void init() throws IOException {
        String batchName = "env_batch.json";
        String batchPath = "env/" + batchName;
        String streamingName = "env_streaming.json";
        String streamingPath = "env/" + streamingName;
        String livyConfName = "sparkProperties.json";
        getBatchEnv(batchName, batchPath, envLocation);
        getStreamingEnv(streamingName, streamingPath, envLocation);
        genLivyConf(livyConfName, livyConfName, configLocation);
    }

    /**
     * Config quartz.properties will be replaced if it's found in external
     * .config.location setting.
     *
     * @return Properties
     * @throws FileNotFoundException It'll throw FileNotFoundException
     *                               when path is wrong.
     */
    @Bean(name = "quartzConf")
    public Properties quartzConf() throws FileNotFoundException {
        String name = "quartz.properties";
        String defaultPath = "/" + name;
        return getConf(name, defaultPath, configLocation);
    }

    private static void genLivyConf(
            String name,
            String defaultPath,
            String location) throws IOException {
        if (livyConfMap != null) {
            return;
        }
        String path = getConfPath(name, location);
        if (path == null) {
            livyConfMap = readPropertiesFromResource(defaultPath);
        } else {
            FileInputStream in = new FileInputStream(path);
            livyConfMap = toEntity(in, new TypeReference<Map>() {
            });
        }
    }

    /**
     * read env config from resource
     *
     * @param path resource path
     * @return Map
     * @throws IOException io exception
     */
    private static Map<String, Object> readPropertiesFromResource(String path)
            throws IOException {
        if (path == null) {
            LOGGER.warn("Parameter path is null.");
            return null;
        }
        // Be careful, here we use getInputStream() to convert path file to
        // stream. It'll cause FileNotFoundException if you use  getFile()
        // to convert path file to File Object
        InputStream in = new ClassPathResource(path).getInputStream();
        return toEntity(in, new TypeReference<Map<String, Object>>() {
        });
    }
}
