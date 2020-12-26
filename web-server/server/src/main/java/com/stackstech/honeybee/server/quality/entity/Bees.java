package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.stackstech.honeybee.server.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY,
//        property = "measure.type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = GriffinMeasure.class, name = "honeybee"),
//        @JsonSubTypes.Type(value = ExternalMeasure.class, name = "external")})
public abstract class Bees extends AbstractAuditableEntity {
    private static final long serialVersionUID = -4748881017029815714L;

    @NotNull
    protected String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String owner;

    @Enumerated(EnumType.STRING)
    private DqType dqType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String organization;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> sinksList = Arrays.asList("ELASTICSEARCH", "HDFS");

    @JsonIgnore
    private String sinks;

    private boolean deleted = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("dq.type")
    public DqType getDqType() {
        return dqType;
    }

    public void setDqType(DqType dqType) {
        this.dqType = dqType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @JsonProperty("sinks")
    public List<String> getSinksList() {
        return sinksList;
    }

    public void setSinksList(List<String> sinksList) {
        this.sinksList = sinksList;
    }

    private String getSinks() {
        return sinks;
    }

    private void setSinks(String sinks) {
        this.sinks = sinks;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @PrePersist
    @PreUpdate
    public void save() throws JsonProcessingException {
        if (sinksList != null) {
            this.sinks = JsonUtil.toJson(sinksList);
        }
    }

    @PostLoad
    public void load() throws IOException {
        if (!StringUtils.isEmpty(sinks)) {
            this.sinksList = JsonUtil.toEntity(sinks, new TypeReference<List<String>>() {
            });
        }
    }

    public Bees() {
    }

    public Bees(String name, String description, String organization,
                String owner) {
        this.name = name;
        this.description = description;
        this.organization = organization;
        this.owner = owner;
    }

    //    @JsonProperty("measure.type")
    public abstract String getType();
}
