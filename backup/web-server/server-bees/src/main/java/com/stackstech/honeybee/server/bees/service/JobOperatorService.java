package com.stackstech.honeybee.server.bees.service;

import com.stackstech.honeybee.server.bees.entity.AbstractJob;
import com.stackstech.honeybee.server.bees.entity.HoneyBees;
import com.stackstech.honeybee.server.bees.entity.JobHealth;
import com.stackstech.honeybee.server.bees.entity.JobState;
import org.quartz.SchedulerException;

public interface JobOperatorService {
    AbstractJob add(AbstractJob job, HoneyBees measure)
            throws Exception;

    void start(AbstractJob job) throws Exception;

    void stop(AbstractJob job) throws SchedulerException;

    void delete(AbstractJob job) throws SchedulerException;

    JobHealth getHealth(JobHealth jobHealth, AbstractJob job)
            throws SchedulerException;

    JobState getState(AbstractJob job, String action)
            throws SchedulerException;
}