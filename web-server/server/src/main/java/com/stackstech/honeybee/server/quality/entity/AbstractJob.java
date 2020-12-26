package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.stackstech.honeybee.server.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "job")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY,
        property = "job.type")
@JsonSubTypes({@JsonSubTypes.Type(value = BatchJob.class, name = "batch"),
        @JsonSubTypes.Type(
                value = StreamingJob.class,
                name = "streaming"),
        @JsonSubTypes.Type(
                value = VirtualJob.class,
                name = "virtual")})
@DiscriminatorColumn(name = "type")
public abstract class AbstractJob extends AbstractAuditableEntity {
    private static final long serialVersionUID = 7569493377868453677L;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractJob.class);

    protected Long measureId;

    protected String jobName;

    protected String metricName;

    @Column(name = "quartz_job_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @Column(name = "quartz_group_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String group;

    @JsonIgnore
    protected boolean deleted = false;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cronExpression;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JobState jobState;

    @NotNull
    private String timeZone;

    @JsonIgnore
    private String predicateConfig;

    @Transient
    private Map<String, Object> configMap;

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = "job_id")
    private List<JobDataSegment> segments = new ArrayList<>();

    @JsonProperty("measure.id")
    public Long getMeasureId() {
        return measureId;
    }

    public void setMeasureId(Long measureId) {
        this.measureId = measureId;
    }

    @JsonProperty("job.name")
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        if (StringUtils.isEmpty(jobName)) {
            LOGGER.warn("Job name cannot be empty.");
            throw new NullPointerException();
        }
        this.jobName = jobName;
    }

    @JsonProperty("cron.expression")
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @JsonProperty("job.state")
    public JobState getJobState() {
        return jobState;
    }

    public void setJobState(JobState jobState) {
        this.jobState = jobState;
    }

    @JsonProperty("cron.time.zone")
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @JsonProperty("data.segments")
    public List<JobDataSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<JobDataSegment> segments) {
        this.segments = segments;
    }

    @JsonProperty("predicate.config")
    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    private String getPredicateConfig() {
        return predicateConfig;
    }

    private void setPredicateConfig(String config) {
        this.predicateConfig = config;
    }

    @JsonProperty("metric.name")
    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("quartz.name")
    public String getName() {
        return name;
    }

    public void setName(String quartzName) {
        this.name = quartzName;
    }

    @JsonProperty("quartz.group")
    public String getGroup() {
        return group;
    }

    public void setGroup(String quartzGroup) {
        this.group = quartzGroup;
    }

    @JsonProperty("job.type")
    public abstract String getType();

    @PrePersist
    @PreUpdate
    public void save() throws JsonProcessingException {
        if (configMap != null) {
            this.predicateConfig = JsonUtil.toJson(configMap);
        }
    }

    @PostLoad
    public void load() throws IOException {
        if (!StringUtils.isEmpty(predicateConfig)) {
            this.configMap = JsonUtil.toEntity(predicateConfig,
                    new TypeReference<Map<String, Object>>() {
                    });
        }
    }

    AbstractJob() {
    }

    AbstractJob(Long measureId, String jobName, String name, String group,
                boolean deleted) {
        this.measureId = measureId;
        this.jobName = jobName;
        this.name = name;
        this.group = group;
        this.deleted = deleted;
    }

    AbstractJob(Long measureId, String jobName, String cronExpression,
                String timeZone, List<JobDataSegment> segments,
                boolean deleted) {
        this.measureId = measureId;
        this.jobName = jobName;
        this.metricName = jobName;
        this.cronExpression = cronExpression;
        this.timeZone = timeZone;
        this.segments = segments;
        this.deleted = deleted;
    }

    AbstractJob(String jobName, Long measureId, String metricName) {
        this.jobName = jobName;
        this.measureId = measureId;
        this.metricName = metricName;
    }
}