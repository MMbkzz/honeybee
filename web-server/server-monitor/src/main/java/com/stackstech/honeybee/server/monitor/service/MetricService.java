package com.stackstech.honeybee.server.monitor.service;


import com.stackstech.honeybee.server.monitor.model.Metric;
import com.stackstech.honeybee.server.monitor.model.MetricValue;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MetricService {

    Map<String, List<Metric>> getAllMetrics();

    List<MetricValue> getMetricValues(String metricName, int offset, int size,
                                      long tmst);

    ResponseEntity addMetricValues(List<MetricValue> values);

    ResponseEntity<?> deleteMetricValues(String metricName);

    MetricValue findMetric(Long id);
}
