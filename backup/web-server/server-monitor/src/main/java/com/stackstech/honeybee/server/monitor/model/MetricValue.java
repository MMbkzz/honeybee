package com.stackstech.honeybee.server.monitor.model;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class MetricValue {

    private String name;

    private Long tmst;

    private Map<String, Object> metadata;

    private Map<String, Object> value;

    public MetricValue() {
    }

    public MetricValue(String name, Long tmst, Map<String, Object> value) {
        this.name = name;
        this.tmst = tmst;
        this.value = value;
        this.metadata = Collections.emptyMap();
    }


    public MetricValue(String name, Long tmst, Map<String, Object> metadata, Map<String, Object> value) {
        this.name = name;
        this.tmst = tmst;
        this.metadata = metadata;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTmst() {
        return tmst;
    }

    public void setTmst(Long tmst) {
        this.tmst = tmst;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricValue that = (MetricValue) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(tmst, that.tmst) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tmst, metadata, value);
    }

    @Override
    public String toString() {
        return String.format(
                "MetricValue{name=%s, ts=%s, meta=%s, value=%s}",
                name, tmst, metadata, value);
    }
}
