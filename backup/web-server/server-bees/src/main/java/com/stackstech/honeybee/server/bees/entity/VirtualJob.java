package com.stackstech.honeybee.server.bees.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("virtualJob")
public class VirtualJob extends AbstractJob {
    private static final long serialVersionUID = 1130038058433818835L;

    @Override
    public String getType() {
        return JobType.VIRTUAL.getName();
    }

    public VirtualJob() {
        super();
    }

    public VirtualJob(String jobName, Long measureId, String metricName) {
        super(jobName, measureId, metricName);
    }
}
