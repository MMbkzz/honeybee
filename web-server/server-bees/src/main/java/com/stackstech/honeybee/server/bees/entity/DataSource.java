package com.stackstech.honeybee.server.bees.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.stackstech.honeybee.server.bees.util.JsonUtil;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.IOException;
import java.util.Map;

@Entity
public class DataSource extends AbstractAuditableEntity {
    private static final long serialVersionUID = -4748881017079815794L;

    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = "data_source_id")
    private DataConnector connector = new DataConnector();

    private boolean baseline = false;

    @JsonIgnore
    @Column(length = 1024)
    private String checkpoint;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> checkpointMap;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataConnector getConnector() {
        return connector;
    }

    public void setConnector(DataConnector connector) {
        this.connector = connector;
    }

    public boolean isBaseline() {
        return baseline;
    }

    public void setBaseline(boolean baseline) {
        this.baseline = baseline;
    }

    private String getCheckpoint() {
        return checkpoint;
    }

    private void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;

    }

    @JsonProperty("checkpoint")
    public Map<String, Object> getCheckpointMap() {
        return checkpointMap;
    }

    public void setCheckpointMap(Map<String, Object> checkpointMap) {
        this.checkpointMap = checkpointMap;
    }

    @PrePersist
    @PreUpdate
    public void save() throws JsonProcessingException {
        if (checkpointMap != null) {
            this.checkpoint = JsonUtil.toJson(checkpointMap);
        }
    }

    @PostLoad
    public void load() throws IOException {
        if (!StringUtils.isEmpty(checkpoint)) {
            this.checkpointMap = JsonUtil.toEntity(
                    checkpoint, new TypeReference<Map<String, Object>>() {
                    });
        }
    }

    public DataSource() {
    }

    public DataSource(String name, DataConnector connector) {
        this.name = name;
        this.connector = connector;
    }

    public DataSource(String name, boolean baseline,
                      Map<String, Object> checkpointMap,
                      DataConnector connector) {
        this.name = name;
        this.baseline = baseline;
        this.checkpointMap = checkpointMap;
        this.connector = connector;

    }
}
