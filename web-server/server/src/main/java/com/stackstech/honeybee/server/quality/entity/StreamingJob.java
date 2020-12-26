package com.stackstech.honeybee.server.quality.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("griffinStreamingJob")
public class StreamingJob extends AbstractJob {
    private static final long serialVersionUID = 3292253488392308505L;

    @Override
    public String getType() {
        return JobType.STREAMING.getName();
    }

    public StreamingJob() {
    }

    public StreamingJob(Long measureId, String jobName, String name,
                        String group, boolean deleted) {
        super(measureId, jobName, name, group, deleted);
        this.metricName = jobName;
    }
}
