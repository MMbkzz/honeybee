package com.stackstech.honeybee.server.quality.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.stackstech.honeybee.server.quality.entity.HoneyBees;
import com.stackstech.honeybee.server.quality.entity.JobInstanceBean;
import com.stackstech.honeybee.server.quality.entity.SegmentPredicate;
import com.stackstech.honeybee.server.quality.factory.PredicatorFactory;
import com.stackstech.honeybee.server.quality.repo.JobInstanceRepo;
import com.stackstech.honeybee.server.quality.service.Predicator;
import com.stackstech.honeybee.server.utils.JsonUtil;
import com.stackstech.honeybee.server.utils.LivyTaskSubmitHelper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stackstech.honeybee.server.quality.conf.EnvConfig.ENV_BATCH;
import static com.stackstech.honeybee.server.quality.conf.EnvConfig.ENV_STREAMING;
import static com.stackstech.honeybee.server.quality.conf.PropertiesConfig.livyConfMap;
import static com.stackstech.honeybee.server.quality.entity.HoneyBees.ProcessType.BATCH;
import static com.stackstech.honeybee.server.quality.entity.LivySessionStates.State;
import static com.stackstech.honeybee.server.quality.entity.LivySessionStates.State.FOUND;
import static com.stackstech.honeybee.server.quality.entity.LivySessionStates.State.NOT_FOUND;
import static com.stackstech.honeybee.server.quality.service.impl.JobInstance.*;
import static com.stackstech.honeybee.server.utils.JsonUtil.toEntity;

/**
 * Simple implementation of the Quartz Job interface, submitting the
 * griffin job to spark cluster via livy
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class SparkSubmitJob implements Job {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SparkSubmitJob.class);

    @Autowired
    private JobInstanceRepo jobInstanceRepo;
    @Autowired
    private BatchJobOperatorImpl batchJobOp;
    @Autowired
    private Environment env;
    @Autowired
    private LivyTaskSubmitHelper livyTaskSubmitHelper;

    @Value("${livy.need.queue:false}")
    private boolean isNeedLivyQueue;
    @Value("${livy.task.appId.retry.count:3}")
    private int appIdRetryCount;

    private HoneyBees measure;
    private String livyUri;
    private List<SegmentPredicate> mPredicates;
    private JobInstanceBean jobInstance;

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jd = context.getJobDetail();
        try {
            if (isNeedLivyQueue) {
                //livy batch limit
                livyTaskSubmitHelper.addTaskToWaitingQueue(jd);
            } else {
                saveJobInstance(jd);
            }
        } catch (Exception e) {
            LOGGER.error("Post spark task ERROR.", e);
        }
    }

    private void updateJobInstanceState(JobExecutionContext context)
            throws IOException {
        SimpleTrigger simpleTrigger = (SimpleTrigger) context.getTrigger();
        int repeatCount = simpleTrigger.getRepeatCount();
        int fireCount = simpleTrigger.getTimesTriggered();
        if (fireCount > repeatCount) {
            saveJobInstance(null, NOT_FOUND);
        }
    }

    private String post2Livy() {
        return livyTaskSubmitHelper.postToLivy(livyUri);
    }

    private boolean success(List<SegmentPredicate> predicates) {
        if (CollectionUtils.isEmpty(predicates)) {
            return true;
        }

        for (SegmentPredicate segPredicate : predicates) {
            Predicator predicator = PredicatorFactory
                    .newPredicateInstance(segPredicate);
            try {
                if (predicator != null && !predicator.predicate()) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private void initParam(JobDetail jd) throws IOException {
        mPredicates = new ArrayList<>();
        jobInstance = jobInstanceRepo.findByPredicateName(jd.getJobDataMap()
                .getString(PREDICATE_JOB_NAME));
        measure = toEntity(jd.getJobDataMap().getString(MEASURE_KEY),
                HoneyBees.class);
        livyUri = env.getProperty("livy.uri");
        setPredicates(jd.getJobDataMap().getString(PREDICATES_KEY));
        // in order to keep metric name unique, we set job name
        // as measure name at present
        measure.setName(jd.getJobDataMap().getString(JOB_NAME));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setPredicates(String json) throws IOException {
        if (StringUtils.isEmpty(json)) {
            return;
        }
        List<SegmentPredicate> predicates = toEntity(json,
                new TypeReference<List<SegmentPredicate>>() {
                });
        if (predicates != null) {
            mPredicates.addAll(predicates);
        }
    }

    private String escapeCharacter(String str, String regex) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String escapeCh = "\\" + regex;
        return str.replaceAll(regex, escapeCh);
    }

    private String genEnv() {
        HoneyBees.ProcessType type = measure.getProcessType();
        String env = type == BATCH ? ENV_BATCH : ENV_STREAMING;
        return env.replaceAll("\\$\\{JOB_NAME}", measure.getName());
    }

    private void setLivyConf() throws IOException {
        setLivyArgs();
    }

    private void setLivyArgs() throws IOException {
        List<String> args = new ArrayList<>();
        args.add(genEnv());
        String measureJson = JsonUtil.toJsonWithFormat(measure);
        // to fix livy bug: character will be ignored by livy
        String finalMeasureJson = escapeCharacter(measureJson, "\\`");
        LOGGER.info(finalMeasureJson);
        args.add(finalMeasureJson);
        args.add("raw,raw");
        livyConfMap.put("args", args);
    }

    public void saveJobInstance(JobDetail jd) throws SchedulerException,
            IOException {
        // If result is null, it may livy uri is wrong
        // or livy parameter is wrong.
        initParam(jd);
        setLivyConf();
        if (!success(mPredicates)) {
            updateJobInstanceState((JobExecutionContext) jd);
            return;
        }
        Map<String, Object> resultMap = post2LivyWithRetry();
        String group = jd.getKey().getGroup();
        String name = jd.getKey().getName();
        batchJobOp.deleteJob(group, name);
        LOGGER.info("Delete predicate job({},{}) SUCCESS.", group, name);
        setJobInstance(resultMap, FOUND);
        jobInstanceRepo.save(jobInstance);
    }

    private Map<String, Object> post2LivyWithRetry()
            throws IOException {
        String result = post2Livy();
        Map<String, Object> resultMap = null;
        if (result != null) {
            resultMap = livyTaskSubmitHelper.retryLivyGetAppId(result, appIdRetryCount);
            if (resultMap != null) {
                livyTaskSubmitHelper.increaseCurTaskNum(Long.valueOf(
                        String.valueOf(resultMap.get("id"))).longValue());
            }
        }

        return resultMap;
    }

    public void saveJobInstance(String result, State state)
            throws IOException {
        TypeReference<HashMap<String, Object>> type =
                new TypeReference<HashMap<String, Object>>() {
                };
        Map<String, Object> resultMap = null;
        if (result != null) {
            resultMap = toEntity(result, type);
        }
        setJobInstance(resultMap, state);
        jobInstanceRepo.save(jobInstance);
    }

    private void setJobInstance(Map<String, Object> resultMap, State state) {
        jobInstance.setState(state);
        jobInstance.setPredicateDeleted(true);
        if (resultMap != null) {
            Object status = resultMap.get("state");
            Object id = resultMap.get("id");
            Object appId = resultMap.get("appId");
            jobInstance.setState(status == null ? null : State.valueOf(status
                    .toString().toUpperCase()));
            jobInstance.setSessionId(id == null ? null : Long.parseLong(id
                    .toString()));
            jobInstance.setAppId(appId == null ? null : appId.toString());
        }
    }

}
