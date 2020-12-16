package com.stackstech.honeybee.server.bees.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory
        .getLogger(FileUtil.class);

    public static String getFilePath(String name, String location) {
        if (StringUtils.isEmpty(location)) {
            LOGGER.info("Location is empty. Read from default path.");
            return null;
        }
        File file = new File(location);
        LOGGER.info("File absolute path:" + file.getAbsolutePath());
        File[] files = file.listFiles();
        if (files == null) {
            LOGGER.warn("The external location '{}' does not exist.Read from"
                + "default path.", location);
            return null;
        }
        return getFilePath(name, files, location);
    }

    private static String getFilePath(String name, File[] files,
                                      String location) {
        String path = null;
        for (File f : files) {
            if (f.getName().equals(name)) {
                path = location + File.separator + name;
                LOGGER.info("config real path: {}", path);
            }
        }
        return path;
    }
}
