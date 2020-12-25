package com.stackstech.honeybee.server.bees.service.impl;

import com.stackstech.honeybee.server.bees.entity.SegmentPredicate;
import com.stackstech.honeybee.server.bees.service.Predicator;
import com.stackstech.honeybee.server.bees.util.FSUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static com.stackstech.honeybee.server.bees.service.impl.JobInstance.PATH_CONNECTOR_CHARACTER;

public class FileExistPredicator implements Predicator {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileExistPredicator.class);

    private static final String PREDICT_PATH = "path";
    private static final String PREDICT_ROOT_PATH = "root.path";

    private SegmentPredicate predicate;

    public FileExistPredicator(SegmentPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean predicate() throws IOException {
        Map<String, Object> config = predicate.getConfigMap();
        String[] paths = null;
        String rootPath = null;
        if (config != null && !StringUtils.isEmpty((String) config.get(PREDICT_PATH))) {
            paths = ((String) config.get(PREDICT_PATH))
                    .split(PATH_CONNECTOR_CHARACTER);
            rootPath = (String) config.get(PREDICT_ROOT_PATH);
        }
        if (ArrayUtils.isEmpty(paths) || StringUtils.isEmpty(rootPath)) {
            LOGGER.error("Predicate path is null.Please check predicates " +
                    "config root.path and path.");
            throw new NullPointerException();
        }
        for (String path : paths) {
            String hdfsPath = rootPath + path;
            LOGGER.info("Predicate path: {}", hdfsPath);
            if (!FSUtil.isFileExist(hdfsPath)) {
                LOGGER.info("Predicate path: " + hdfsPath + " doesn't exist.");
                return false;
            }
            LOGGER.info("Predicate path: " + hdfsPath + " exists.");
        }
        return true;
    }
}
