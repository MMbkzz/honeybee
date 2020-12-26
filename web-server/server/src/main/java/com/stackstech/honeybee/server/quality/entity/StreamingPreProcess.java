package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.stackstech.honeybee.server.utils.JsonUtil;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.IOException;
import java.util.Map;

@Entity
public class StreamingPreProcess extends AbstractAuditableEntity {
    private static final long serialVersionUID = -7471448761795495384L;

    private String dslType;

    private String inDataFrameName;

    private String outDataFrameName;

    private String rule;

    @JsonIgnore
    @Column(length = 1024)
    private String details;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> detailsMap;

    @JsonProperty(("dsl.type"))
    public String getDslType() {
        return dslType;
    }

    public void setDslType(String dslType) {
        this.dslType = dslType;
    }

    @JsonProperty("in.dataframe.name")
    public String getInDataFrameName() {
        return inDataFrameName;
    }

    public void setInDataFrameName(String inDataFrameName) {
        this.inDataFrameName = inDataFrameName;
    }

    @JsonProperty("out.dataframe.name")
    public String getOutDataFrameName() {
        return outDataFrameName;
    }

    public void setOutDataFrameName(String outDataFrameName) {
        this.outDataFrameName = outDataFrameName;
    }


    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    private String getDetails() {
        return details;
    }

    private void setDetails(String details) {
        this.details = details;
    }

    @JsonProperty("details")
    public Map<String, Object> getDetailsMap() {
        return detailsMap;
    }

    public void setDetailsMap(Map<String, Object> details) {
        this.detailsMap = details;
    }

    @PrePersist
    @PreUpdate
    public void save() throws JsonProcessingException {
        if (detailsMap != null) {
            this.details = JsonUtil.toJson(detailsMap);
        }
    }

    @PostLoad
    public void load() throws IOException {
        if (!StringUtils.isEmpty(details)) {
            this.detailsMap = JsonUtil.toEntity(details,
                    new TypeReference<Map<String, Object>>() {
                    });
        }
    }

}
