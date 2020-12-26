package com.stackstech.honeybee.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static com.stackstech.honeybee.server.utils.FileUtil.getFilePath;

public class PropertiesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            PropertiesUtil.class);

    public static Properties getProperties(String path, Resource resource) {
        PropertiesFactoryBean propFactoryBean = new PropertiesFactoryBean();
        Properties properties = null;
        try {
            propFactoryBean.setLocation(resource);
            propFactoryBean.afterPropertiesSet();
            properties = propFactoryBean.getObject();
            LOGGER.info("Read properties successfully from {}.", path);
        } catch (IOException e) {
            LOGGER.error("Get properties from {} failed. {}", path, e);
        }
        return properties;
    }

    /**
     * @param name        properties name like quartz.properties
     * @param defaultPath properties classpath like /quartz.properties
     * @param location    custom properties path
     * @return Properties
     * @throws FileNotFoundException location setting is wrong that there is no
     *                               target file.
     */
    public static Properties getConf(String name, String defaultPath,
                                     String location)
            throws FileNotFoundException {
        String path = getConfPath(name, location);
        Resource resource;
        if (path == null) {
            resource = new ClassPathResource(defaultPath);
            path = defaultPath;
        } else {
            resource = new InputStreamResource(new FileInputStream(path));
        }
        return getProperties(path, resource);
    }

    public static String getConfPath(String name, String location) {
        return getFilePath(name, location);
    }


}
