package com.stackstech.honeybee.server.utils;


import com.stackstech.honeybee.server.core.exception.BeesException;
import com.stackstech.honeybee.server.quality.entity.*;
import com.stackstech.honeybee.server.quality.factory.PredicatorFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.stackstech.honeybee.server.core.exception.BeesExceptionMessage.*;

public class BeesUtil {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeesUtil.class);

    public static void validateMeasure(Bees measure) {
        if (measure instanceof HoneyBees) {
            validateBees((HoneyBees) measure);
        } else if (measure instanceof ExternalBees) {
            validateExternalBees((ExternalBees) measure);
        }

    }

    private static void validateBees(HoneyBees measure) {
        if (getConnectorNamesIfValid(measure) == null) {
            throw new BeesException.BadRequestException
                    (INVALID_CONNECTOR_NAME);
        }
        if (!validatePredicates(measure)) {
            throw new BeesException.BadRequestException(INVALID_MEASURE_PREDICATE);
        }
    }

    private static boolean validatePredicates(HoneyBees measure) {
        for (DataSource dataSource : measure.getDataSources()) {
            for (SegmentPredicate segmentPredicate : dataSource.getConnector().getPredicates()) {
                try {
                    PredicatorFactory.newPredicateInstance(segmentPredicate);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void validateExternalBees(ExternalBees measure) {
        if (StringUtils.isBlank(measure.getMetricName())) {
            LOGGER.warn("Failed to create external measure {}. " +
                    "Its metric name is blank.", measure.getName());
            throw new BeesException.BadRequestException(MISSING_METRIC_NAME);
        }
    }

    private static List<String> getConnectorNamesIfValid(HoneyBees measure) {
        Set<String> sets = new HashSet<>();
        List<DataSource> sources = measure.getDataSources();
        for (DataSource source : sources) {
            if (source.getConnector() != null && source.getConnector().getName() != null) {
                sets.add(source.getConnector().getName());
            }
        }
        if (sets.size() == 0 || sets.size() < sources.size()) {
            LOGGER.warn("Connector names cannot be repeated or empty.");
            return null;
        }
        return new ArrayList<>(sets);
    }
}
