package com.stackstech.honeybee.server.bees.service.impl;

import com.stackstech.honeybee.server.bees.entity.*;
import com.stackstech.honeybee.server.bees.exception.BeesException;
import com.stackstech.honeybee.server.bees.repo.BatchJobRepo;
import com.stackstech.honeybee.server.bees.repo.JobInstanceRepo;
import com.stackstech.honeybee.server.bees.service.JobOperatorService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.stackstech.honeybee.server.bees.entity.HoneyBees.ProcessType.BATCH;
import static com.stackstech.honeybee.server.bees.exception.BeesExceptionMessage.*;
import static org.quartz.CronExpression.isValidExpression;
import static org.quartz.JobKey.jobKey;
import static org.quartz.Trigger.TriggerState;
import static org.quartz.Trigger.TriggerState.*;
import static org.quartz.TriggerKey.triggerKey;

@Service
public class BatchJobOperatorImpl implements JobOperatorService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BatchJobOperatorImpl.class);

    @Autowired
    @Qualifier("schedulerFactoryBean")
    private SchedulerFactoryBean factory;
    @Autowired
    private JobInstanceRepo instanceRepo;
    @Autowired
    private BatchJobRepo batchJobRepo;
    @Autowired
    private JobServiceImpl jobService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AbstractJob add(AbstractJob job, HoneyBees measure)
            throws Exception {
        validateParams(job, measure);
        String qName = jobService.getQuartzName(job);
        String qGroup = jobService.getQuartzGroup();
        TriggerKey triggerKey = jobService.getTriggerKeyIfValid(qName, qGroup);
        BatchJob batchJob = genBatchJobBean(job, qName, qGroup);
        batchJob = batchJobRepo.save(batchJob);
        jobService.addJob(triggerKey, batchJob, BATCH);
        return job;
    }

    private BatchJob genBatchJobBean(AbstractJob job,
                                     String qName,
                                     String qGroup) {
        BatchJob batchJob = (BatchJob) job;
        batchJob.setMetricName(job.getJobName());
        batchJob.setGroup(qGroup);
        batchJob.setName(qName);
        return batchJob;
    }

    /**
     * all states: BLOCKED  COMPLETE ERROR NONE  NORMAL   PAUSED
     * to start states: PAUSED
     * to stop states: BLOCKED   NORMAL
     *
     * @param job streaming job
     */
    @Override
    public void start(AbstractJob job) {
        String name = job.getName();
        String group = job.getGroup();
        TriggerState state = getTriggerState(name, group);
        if (state == null) {
            throw new BeesException.BadRequestException(
                    JOB_IS_NOT_SCHEDULED);
        }
        /* If job is not in paused state,we can't start it
        as it may be RUNNING.*/
        if (state != PAUSED) {
            throw new BeesException.BadRequestException
                    (JOB_IS_NOT_IN_PAUSED_STATUS);
        }
        JobKey jobKey = jobKey(name, group);
        try {
            factory.getScheduler().resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new BeesException.ServiceException(
                    "Failed to start job.", e);
        }
    }

    @Override
    public void stop(AbstractJob job) {
        pauseJob((BatchJob) job, false);
    }

    @Override
    @Transactional
    public void delete(AbstractJob job) {
        pauseJob((BatchJob) job, true);
    }


    @Override
    public JobHealth getHealth(JobHealth jobHealth, AbstractJob job)
            throws SchedulerException {
        List<? extends Trigger> triggers = jobService
                .getTriggers(job.getName(), job.getGroup());
        if (!CollectionUtils.isEmpty(triggers)) {
            jobHealth.setJobCount(jobHealth.getJobCount() + 1);
            if (jobService.isJobHealthy(job.getId())) {
                jobHealth.setHealthyJobCount(
                        jobHealth.getHealthyJobCount() + 1);
            }
        }
        return jobHealth;
    }

    @Override
    public JobState getState(AbstractJob job, String action)
            throws SchedulerException {
        JobState jobState = new JobState();
        Scheduler scheduler = factory.getScheduler();
        if (job.getGroup() == null || job.getName() == null) {
            return null;
        }
        TriggerKey triggerKey = triggerKey(job.getName(), job.getGroup());
        TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        jobState.setState(triggerState.toString());
        jobState.setToStart(getStartStatus(triggerState));
        jobState.setToStop(getStopStatus(triggerState));
        setTriggerTime(job, jobState);
        return jobState;
    }

    private void setTriggerTime(AbstractJob job, JobState jobState)
            throws SchedulerException {
        List<? extends Trigger> triggers = jobService
                .getTriggers(job.getName(), job.getGroup());
        // If triggers are empty, in Griffin it means job is completed whose
        // trigger state is NONE or not scheduled.
        if (CollectionUtils.isEmpty(triggers)) {
            return;
        }
        Trigger trigger = triggers.get(0);
        Date nextFireTime = trigger.getNextFireTime();
        Date previousFireTime = trigger.getPreviousFireTime();
        jobState.setNextFireTime(nextFireTime != null ?
                nextFireTime.getTime() : -1);
        jobState.setPreviousFireTime(previousFireTime != null ?
                previousFireTime.getTime() : -1);
    }

    /**
     * only PAUSED state of job can be started
     *
     * @param state job state
     * @return true: job can be started, false: job is running which cannot be
     * started
     */
    private boolean getStartStatus(TriggerState state) {
        return state == PAUSED;
    }

    /**
     * only NORMAL or  BLOCKED state of job can be started
     *
     * @param state job state
     * @return true: job can be stopped, false: job is running which cannot be
     * stopped
     */
    private boolean getStopStatus(TriggerState state) {
        return state == NORMAL || state == BLOCKED;
    }


    private TriggerState getTriggerState(String name, String group) {
        try {
            List<? extends Trigger> triggers = jobService.getTriggers(name,
                    group);
            if (CollectionUtils.isEmpty(triggers)) {
                return null;
            }
            TriggerKey key = triggers.get(0).getKey();
            return factory.getScheduler().getTriggerState(key);
        } catch (SchedulerException e) {
            LOGGER.error("Failed to delete job", e);
            throw new BeesException
                    .ServiceException("Failed to delete job", e);
        }

    }


    /**
     * @param job    griffin job
     * @param delete if job needs to be deleted,set isNeedDelete true,otherwise
     *               it just will be paused.
     */
    private void pauseJob(BatchJob job, boolean delete) {
        try {
            pauseJob(job.getGroup(), job.getName());
            pausePredicateJob(job);
            job.setDeleted(delete);
            batchJobRepo.save(job);
        } catch (Exception e) {
            LOGGER.error("Job schedule happens exception.", e);
            throw new BeesException.ServiceException("Job schedule " +
                    "happens exception.", e);
        }
    }

    private void pausePredicateJob(BatchJob job) throws SchedulerException {
        List<JobInstanceBean> instances = instanceRepo.findByJobId(job.getId());
        for (JobInstanceBean instance : instances) {
            if (!instance.isPredicateDeleted()) {
                deleteJob(instance.getPredicateGroup(), instance
                        .getPredicateName());
                instance.setPredicateDeleted(true);
                if (instance.getState().equals(LivySessionStates.State.FINDING)) {
                    instance.setState(LivySessionStates.State.NOT_FOUND);
                }
            }
        }
    }

    public void deleteJob(String group, String name) throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        JobKey jobKey = new JobKey(name, group);
        if (!scheduler.checkExists(jobKey)) {
            LOGGER.info("Job({},{}) does not exist.", jobKey.getGroup(), jobKey
                    .getName());
            return;
        }
        scheduler.deleteJob(jobKey);

    }

    private void pauseJob(String group, String name) throws SchedulerException {
        if (StringUtils.isEmpty(group) || StringUtils.isEmpty(name)) {
            return;
        }
        Scheduler scheduler = factory.getScheduler();
        JobKey jobKey = new JobKey(name, group);
        if (!scheduler.checkExists(jobKey)) {
            LOGGER.warn("Job({},{}) does not exist.", jobKey.getGroup(), jobKey
                    .getName());
            throw new BeesException.NotFoundException
                    (JOB_KEY_DOES_NOT_EXIST);
        }
        scheduler.pauseJob(jobKey);
    }

    public boolean pauseJobInstances(List<JobInstanceBean> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            return true;
        }
        List<JobInstanceBean> deletedInstances = new ArrayList<>();
        boolean pauseStatus = true;
        for (JobInstanceBean instance : instances) {
            boolean status = pauseJobInstance(instance, deletedInstances);
            pauseStatus = pauseStatus && status;
        }
        instanceRepo.saveAll(deletedInstances);
        return pauseStatus;
    }

    private boolean pauseJobInstance(JobInstanceBean instance,
                                     List<JobInstanceBean> deletedInstances) {
        String pGroup = instance.getPredicateGroup();
        String pName = instance.getPredicateName();
        try {
            if (!instance.isPredicateDeleted()) {
                deleteJob(pGroup, pName);
                instance.setPredicateDeleted(true);
                deletedInstances.add(instance);
            }
        } catch (SchedulerException e) {
            LOGGER.error("Failed to pause predicate job({},{}).", pGroup,
                    pName);
            return false;
        }
        return true;
    }

    private void validateParams(AbstractJob job, HoneyBees measure) {
        if (!jobService.isValidJobName(job.getJobName())) {
            throw new BeesException.BadRequestException(INVALID_JOB_NAME);
        }
        if (!isValidCronExpression(job.getCronExpression())) {
            throw new BeesException.BadRequestException
                    (INVALID_CRON_EXPRESSION);
        }
        if (!isValidBaseLine(job.getSegments())) {
            throw new BeesException.BadRequestException
                    (MISSING_BASELINE_CONFIG);
        }
        List<String> names = getConnectorNames(measure);
        if (!isValidConnectorNames(job.getSegments(), names)) {
            throw new BeesException.BadRequestException
                    (INVALID_CONNECTOR_NAME);
        }
    }

    private boolean isValidCronExpression(String cronExpression) {
        if (StringUtils.isEmpty(cronExpression)) {
            LOGGER.warn("Cron Expression is empty.");
            return false;
        }
        if (!isValidExpression(cronExpression)) {
            LOGGER.warn("Cron Expression is invalid: {}", cronExpression);
            return false;
        }
        return true;
    }

    private boolean isValidBaseLine(List<JobDataSegment> segments) {
        assert segments != null;
        for (JobDataSegment jds : segments) {
            if (jds.isAsTsBaseline()) {
                return true;
            }
        }
        LOGGER.warn("Please set segment timestamp baseline " +
                "in as.baseline field.");
        return false;
    }

    private boolean isValidConnectorNames(List<JobDataSegment> segments,
                                          List<String> names) {
        assert segments != null;
        Set<String> sets = new HashSet<>();
        for (JobDataSegment segment : segments) {
            String dcName = segment.getDataConnectorName();
            sets.add(dcName);
            boolean exist = names.stream().anyMatch(name -> name.equals
                    (dcName));
            if (!exist) {
                LOGGER.warn("Param {} is a illegal string. " +
                        "Please input one of strings in {}.", dcName, names);
                return false;
            }
        }
        if (sets.size() < segments.size()) {
            LOGGER.warn("Connector names in job data segment " +
                    "cannot duplicate.");
            return false;
        }
        return true;
    }

    private List<String> getConnectorNames(HoneyBees measure) {
        Set<String> sets = new HashSet<>();
        List<DataSource> sources = measure.getDataSources();
        for (DataSource source : sources) {
            sets.add(source.getConnector().getName());
        }
        if (sets.size() < sources.size()) {
            LOGGER.warn("Connector names cannot be repeated.");
            return Collections.emptyList();
        }
        return new ArrayList<>(sets);
    }
}
