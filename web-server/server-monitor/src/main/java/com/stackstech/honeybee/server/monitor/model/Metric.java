package com.stackstech.honeybee.server.monitor.model;


import com.stackstech.honeybee.server.bees.entity.DqType;

import java.util.List;

public class Metric {

    private String name;
    private DqType type;
    private String owner;
    private List<MetricValue> metricValues;

    public Metric() {
    }

    public Metric(String name, DqType type, String owner,
                  List<MetricValue> metricValues) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.metricValues = metricValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DqType getType() {
        return type;
    }

    public void setType(DqType type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<MetricValue> getMetricValues() {
        return metricValues;
    }

    public void setMetricValues(List<MetricValue> metricValues) {
        this.metricValues = metricValues;
    }
}
