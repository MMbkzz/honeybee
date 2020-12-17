package com.stackstech.honeybee.server.bees.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.stackstech.honeybee.server.bees.util.JsonUtil;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.IOException;
import java.util.Map;

@Entity
public class SegmentPredicate extends AbstractAuditableEntity {

    private static final long serialVersionUID = 1942715275465116154L;

    private String type;

    @JsonIgnore
    private String config;

    @Transient
    private Map<String, Object> configMap;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("config")
    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    private String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @PrePersist
    @PreUpdate
    public void save() throws JsonProcessingException {
        if (configMap != null) {
            this.config = JsonUtil.toJson(configMap);
        }
    }

    @PostLoad
    public void load() throws IOException {
        if (!StringUtils.isEmpty(config)) {
            this.configMap = JsonUtil.toEntity(config,
                    new TypeReference<Map<String, Object>>() {
                    });
        }
    }

    public SegmentPredicate() {
    }

    public SegmentPredicate(String type, Map<String, String> configMap)
            throws JsonProcessingException {
        this.type = type;
        this.config = JsonUtil.toJson(configMap);
    }
}
