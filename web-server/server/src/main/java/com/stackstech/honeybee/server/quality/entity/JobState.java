package com.stackstech.honeybee.server.quality.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Encapsulating job scheduler state to reduce job startup and stop logical
 * processing
 */
public class JobState {

    /**
     * job scheduler state
     */
    private String state;

    /**
     * whether job can be started
     */
    private boolean toStart = false;

    /**
     * whether job can be stopped
     */
    private boolean toStop = false;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long nextFireTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long previousFireTime;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isToStart() {
        return toStart;
    }

    public void setToStart(boolean toStart) {
        this.toStart = toStart;
    }

    public boolean isToStop() {
        return toStop;
    }

    public void setToStop(boolean toStop) {
        this.toStop = toStop;
    }

    public Long getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Long nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Long getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(Long previousFireTime) {
        this.previousFireTime = previousFireTime;
    }
}
