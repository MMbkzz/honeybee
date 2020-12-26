package com.stackstech.honeybee.server.quality.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue("griffinBatchJob")
public class BatchJob extends AbstractJob {
    private static final long serialVersionUID = -1114269860236729008L;

    @Override
    public String getType() {
        return JobType.BATCH.getName();
    }

    public BatchJob() {
        super();
    }

    public BatchJob(Long measureId, String jobName, String name, String group,
                    boolean deleted) {
        super(measureId, jobName, name, group, deleted);
        this.metricName = jobName;
    }

    public BatchJob(Long jobId, Long measureId, String jobName, String qJobName,
                    String qGroupName, boolean deleted) {
        this(measureId, jobName, qJobName, qGroupName, deleted);
        setId(jobId);
    }

    public BatchJob(Long measureId, String jobName, String cronExpression,
                    String timeZone, List<JobDataSegment> segments,
                    boolean deleted) {
        super(measureId, jobName, cronExpression, timeZone, segments, deleted);
    }

}
