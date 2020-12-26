package com.stackstech.honeybee.server.quality.service;

import com.stackstech.honeybee.server.quality.entity.AbstractJob;
import com.stackstech.honeybee.server.quality.entity.JobHealth;
import com.stackstech.honeybee.server.quality.entity.JobInstanceBean;
import org.quartz.SchedulerException;

import java.util.List;

public interface JobService {

    List<AbstractJob> getAliveJobs(String type);

    AbstractJob addJob(AbstractJob js) throws Exception;

    AbstractJob getJobConfig(Long jobId);

    AbstractJob onAction(Long jobId, String action) throws Exception;

    void deleteJob(Long jobId) throws SchedulerException;

    void deleteJob(String jobName) throws SchedulerException;

    List<JobInstanceBean> findInstancesOfJob(Long jobId, int page, int size);

    List<JobInstanceBean> findInstancesByTriggerKey(String triggerKey);

    JobHealth getHealthInfo();

    String getJobHdfsSinksPath(String jobName, long timestamp);

    JobInstanceBean findInstance(Long id);

    String triggerJobById(Long id) throws SchedulerException;
}
