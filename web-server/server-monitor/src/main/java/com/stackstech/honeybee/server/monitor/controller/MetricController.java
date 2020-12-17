package com.stackstech.honeybee.server.monitor.controller;

import com.stackstech.honeybee.server.monitor.model.Metric;
import com.stackstech.honeybee.server.monitor.model.MetricValue;
import com.stackstech.honeybee.server.monitor.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MetricController {

    @Autowired
    private MetricService metricService;

    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    public Map<String, List<Metric>> getAllMetrics() {
        return metricService.getAllMetrics();
    }

    @RequestMapping(value = "/metrics/values", method = RequestMethod.GET)
    public List<MetricValue> getMetricValues(@RequestParam("metricName")
                                                     String metricName,
                                             @RequestParam("size") int size,
                                             @RequestParam(value = "offset",
                                                     defaultValue = "0")
                                                     int offset,
                                             @RequestParam(value = "tmst",
                                                     defaultValue = "0")
                                                     long tmst) {
        return metricService.getMetricValues(metricName, offset, size, tmst);
    }

    @RequestMapping(value = "/metrics/values", method = RequestMethod.POST)
    public ResponseEntity<?> addMetricValues(@RequestBody List<MetricValue>
                                                     values) {
        return metricService.addMetricValues(values);
    }

    @RequestMapping(value = "/metrics/values", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteMetricValues(@RequestParam("metricName")
                                                        String metricName) {
        return metricService.deleteMetricValues(metricName);
    }

    @RequestMapping(value = "/metrics/values/{instanceId}", method = RequestMethod.GET)
    public MetricValue getMetric(@PathVariable("instanceId") Long id) {
        return metricService.findMetric(id);
    }
}
