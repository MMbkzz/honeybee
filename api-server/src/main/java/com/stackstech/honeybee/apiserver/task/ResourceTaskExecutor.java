package com.stackstech.honeybee.apiserver.task;

import com.stackstech.honeybee.apiserver.service.AuditLogService;
import com.stackstech.honeybee.apiserver.service.ResourceService;
import com.stackstech.honeybee.core.constants.CronConstant;
import com.stackstech.honeybee.core.enums.TaskEnum;
import com.stackstech.honeybee.core.enums.TaskStatusEnum;
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
public class ResourceTaskExecutor implements SchedulingConfigurer {

    final Logger logger = LoggerFactory.getLogger(ResourceTaskExecutor.class);

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private AuditLogService auditLogService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() ->{
            auditLogService.logBegin(TaskEnum.instanceResource.toString());
            try {
                resourceService.configResource();
            } catch (Exception e) {
                logger.error("节点任务 - 资源初始化异常", e);
                auditLogService.logStatus(TaskStatusEnum.err.code);
                auditLogService.logContext(ExceptionUtils.getFullStackTrace(e));
            }
            auditLogService.logEnd();
            auditLogService.insertLogs();
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(CronConstant.NODE_RESOURCE);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
