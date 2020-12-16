package com.stackstech.honeybee.server.bees.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import static com.stackstech.honeybee.server.bees.entity.GriffinMeasure.ProcessType.BATCH;

@Entity
//TODO @Table(indexes = {@Index(columnList = "triggerKey")})
public class JobInstanceBean extends AbstractAuditableEntity {

    private static final long serialVersionUID = -4748881017029815874L;

    private Long sessionId;

    @Enumerated(EnumType.STRING)
    private LivySessionStates.State state;

    @Enumerated(EnumType.STRING)
    private GriffinMeasure.ProcessType type = BATCH;

    /**
     * The application id of this session
     **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String appId;

    @Column(length = 2 * 1024)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String appUri;

    @Column(name = "timestamp")
    private Long tms;

    @Column(name = "expire_timestamp")
    private Long expireTms;

    @Column(name = "predicate_group_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String predicateGroup;

    @Column(name = "predicate_job_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String predicateName;

    @Column(name = "predicate_job_deleted")
    @JsonIgnore
    private boolean predicateDeleted = false;

    @JsonIgnore
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @JsonIgnore
    private AbstractJob job;

    private String triggerKey;

    public AbstractJob getJob() {
        return job;
    }

    public void setJob(AbstractJob job) {
        this.job = job;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public LivySessionStates.State getState() {
        return state;
    }

    public void setState(LivySessionStates.State state) {
        this.state = state;
    }

    public GriffinMeasure.ProcessType getType() {
        return type;
    }

    public void setType(GriffinMeasure.ProcessType type) {
        this.type = type;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppUri() {
        return appUri;
    }

    public void setAppUri(String appUri) {
        this.appUri = appUri;
    }

    @JsonProperty("timestamp")
    public Long getTms() {
        return tms;
    }

    public void setTms(Long tms) {
        this.tms = tms;
    }

    @JsonProperty("expireTimestamp")
    public Long getExpireTms() {
        return expireTms;
    }

    public void setExpireTms(Long expireTms) {
        this.expireTms = expireTms;
    }

    public String getPredicateGroup() {
        return predicateGroup;
    }

    public void setPredicateGroup(String predicateGroup) {
        this.predicateGroup = predicateGroup;
    }

    public String getPredicateName() {
        return predicateName;
    }

    public void setPredicateName(String predicateName) {
        this.predicateName = predicateName;
    }

    public boolean isPredicateDeleted() {
        return predicateDeleted;
    }

    public void setPredicateDeleted(boolean predicateDeleted) {
        this.predicateDeleted = predicateDeleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getTriggerKey() {
        return triggerKey;
    }

    public void setTriggerKey(String triggerKey) {
        this.triggerKey = triggerKey;
    }

    public JobInstanceBean() {
    }

    public JobInstanceBean(LivySessionStates.State state, Long tms, Long expireTms, String appId) {
        this.state = state;
        this.tms = tms;
        this.expireTms = expireTms;
        this.appId = appId;
    }

    public JobInstanceBean(LivySessionStates.State state, Long tms, Long expireTms) {
        this.state = state;
        this.tms = tms;
        this.expireTms = expireTms;
    }

    public JobInstanceBean(LivySessionStates.State state, String pName, String pGroup, Long tms,
                           Long expireTms) {
        this.state = state;
        this.predicateName = pName;
        this.predicateGroup = pGroup;
        this.tms = tms;
        this.expireTms = expireTms;
    }

    public JobInstanceBean(LivySessionStates.State state, String pName, String pGroup, Long tms,
                           Long expireTms, AbstractJob job) {
        this(state, pName, pGroup, tms, expireTms);
        this.job = job;
    }

    public JobInstanceBean(LivySessionStates.State state, String pName, String pGroup, Long tms,
                           Long expireTms, GriffinMeasure.ProcessType type) {
        this(state, pName, pGroup, tms, expireTms);
        this.type = type;
    }

    public JobInstanceBean(Long sessionId, LivySessionStates.State state, String appId,
                           String appUri, Long timestamp, Long expireTms) {
        this.sessionId = sessionId;
        this.state = state;
        this.appId = appId;
        this.appUri = appUri;
        this.tms = timestamp;
        this.expireTms = expireTms;
    }
}
