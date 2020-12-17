package com.stackstech.honeybee.server.bees.controller;

import com.stackstech.honeybee.server.bees.entity.AbstractJob;
import com.stackstech.honeybee.server.bees.entity.JobHealth;
import com.stackstech.honeybee.server.bees.entity.JobInstanceBean;
import com.stackstech.honeybee.server.bees.service.JobService;
import com.stackstech.honeybee.server.bees.util.FSUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public List<AbstractJob> getJobs(@RequestParam(value = "type",
            defaultValue = "") String type) {
        return jobService.getAliveJobs(type);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AbstractJob addJob(@RequestBody AbstractJob job) throws Exception {
        return jobService.addJob(job);
    }

    @RequestMapping(value = "/jobs/config")
    public AbstractJob getJobConfig(@RequestParam("jobId") Long jobId) {
        return jobService.getJobConfig(jobId);
    }

    @RequestMapping(value = "/jobs/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public AbstractJob onActions(
            @PathVariable("id") Long jobId,
            @RequestParam String action) throws Exception {
        return jobService.onAction(jobId, action);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJob(@RequestParam("jobName") String jobName)
            throws SchedulerException {
        jobService.deleteJob(jobName);
    }

    @RequestMapping(value = "/jobs/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJob(@PathVariable("id") Long id)
            throws SchedulerException {
        jobService.deleteJob(id);
    }

    @RequestMapping(value = "/jobs/instances", method = RequestMethod.GET)
    public List<JobInstanceBean> findInstancesOfJob(
            @RequestParam("jobId") Long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return jobService.findInstancesOfJob(id, page, size);
    }

    @RequestMapping(value = "/jobs/instances/{instanceId}", method = RequestMethod.GET)
    public JobInstanceBean findInstanceByInstanceId(@PathVariable("instanceId") Long id) {
        return jobService.findInstance(id);
    }

    @RequestMapping(value = "/jobs/health", method = RequestMethod.GET)
    public JobHealth getHealthInfo() {
        return jobService.getHealthInfo();
    }

    @RequestMapping(path = "/jobs/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(
            @RequestParam("jobName") String jobName,
            @RequestParam("ts") long timestamp)
            throws Exception {
        String path = jobService.getJobHdfsSinksPath(jobName, timestamp);
        InputStreamResource resource = new InputStreamResource(
                FSUtil.getMissSampleInputStream(path));
        return ResponseEntity.ok().
                header("content-disposition",
                        "attachment; filename = sampleMissingData.json")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @RequestMapping(value = "/jobs/trigger/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> triggerJob(@PathVariable("id") Long id, @RequestBody(required = false) String request) throws SchedulerException {
        return Collections.singletonMap("triggerKey", jobService.triggerJobById(id));
    }

    @RequestMapping(value = "jobs/triggerKeys/{triggerKey:.+}", method = RequestMethod.GET)
    public List<JobInstanceBean> findInstanceByTriggerKey(@PathVariable("triggerKey") String triggerKey) {
        return jobService.findInstancesByTriggerKey(triggerKey);
    }
}
