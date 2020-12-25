package com.stackstech.honeybee.server.monitor.service;

import com.stackstech.honeybee.server.monitor.model.MetricValue;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface MetricStore {

    List<MetricValue> getMetricValues(String metricName, int from, int size,
                                      long tmst) throws IOException;

    ResponseEntity<?> addMetricValues(List<MetricValue> metricValues)
            throws IOException;

    ResponseEntity<?> deleteMetricValues(String metricName) throws IOException;

    MetricValue getMetric(String applicationId) throws IOException;
}
