package com.stackstech.honeybee.common.entity;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Request parameter base map
 *
 * @author william
 */
public class ParameterMap<K, V> extends ForwardingMap<K, V> implements Serializable {

    private Map<K, V> parameter;

    public ParameterMap() {
        this(Maps.newHashMap());
    }

    public ParameterMap(Map<K, V> parameter) {
        super();
        this.parameter = parameter;
    }

    @Override
    protected Map<K, V> delegate() {
        return parameter;
    }

    public Map<K, V> getParameter() {
        return parameter;
    }

    public void setParameter(Map<K, V> parameter) {
        this.parameter = parameter;
    }

    public String getString(String key) {
        return (String) super.get(key);
    }

    public Integer getInteger(String key) {
        return (Integer) super.get(key);
    }

    public Long getLong(String key) {
        return (Long) super.get(key);
    }

    public Boolean getBoolean(String key) {
        return (Boolean) super.get(key);
    }

    public Date getDate(String key) {
        return (Date) super.get(key);
    }

    @Override
    public String toString() {
        return parameter + "";
    }
}
