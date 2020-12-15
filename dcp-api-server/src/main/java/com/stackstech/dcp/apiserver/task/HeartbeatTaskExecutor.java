package com.stackstech.dcp.apiserver.task;

import com.stackstech.dcp.apiserver.conf.ServerConfig;
import com.stackstech.dcp.apiserver.service.AuditLogService;
import com.stackstech.dcp.core.cache.InstanceHeartbeatCache;
import com.stackstech.dcp.core.constants.CronConstant;
import com.stackstech.dcp.core.enums.TaskEnum;
import com.stackstech.dcp.core.enums.TaskStatusEnum;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Lazy(false)
@Component
@EnableScheduling
public class HeartbeatTaskExecutor implements SchedulingConfigurer {

    final Logger logger = LoggerFactory.getLogger(HeartbeatTaskExecutor.class);

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private InstanceHeartbeatCache instanceHeartbeatCache;
    @Autowired
    private AuditLogService auditLogService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            auditLogService.logBegin(TaskEnum.instanceHeartbeat.toString());
            try {
                instanceHeartbeatCache.put(null, serverConfig.getReal(), System.currentTimeMillis());
            } catch (Exception e) {
                logger.error("节点任务 - 发送心跳包异常", e);
                auditLogService.logStatus(TaskStatusEnum.err.code);
                auditLogService.logContext(ExceptionUtils.getFullStackTrace(e));
            }
            auditLogService.logEnd();
            auditLogService.insertLogs();
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CronConstant.NODE_HEARTBEAT);
            return trigger.nextExecutionTime(triggerContext);
        });
    }

}
