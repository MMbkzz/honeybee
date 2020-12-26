package com.stackstech.honeybee.server.quality.entity;


/**
 * @author william
 */
public class JobHealth {
    private int healthyJobCount;
    private int jobCount;

    public int getHealthyJobCount() {
        return healthyJobCount;
    }

    public void setHealthyJobCount(int healthyJobCount) {
        this.healthyJobCount = healthyJobCount;
    }

    public int getJobCount() {
        return jobCount;
    }

    public void setJobCount(int jobCount) {
        this.jobCount = jobCount;
    }

    public JobHealth(int healthyJobCount, int jobCount) {
        this.healthyJobCount = healthyJobCount;
        this.jobCount = jobCount;
    }

    public JobHealth() {
    }
}
