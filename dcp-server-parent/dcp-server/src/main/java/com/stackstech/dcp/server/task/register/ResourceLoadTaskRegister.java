package com.stackstech.dcp.server.task.register;

import com.stackstech.dcp.core.constants.CronConstant;
import com.stackstech.dcp.server.task.ResourceLoadTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Lazy(false)
@Component
@EnableScheduling
public class ResourceLoadTaskRegister implements SchedulingConfigurer {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ResourceLoadTask resourceTask;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(resourceTask, triggerContext -> {
            // 任务触发，可修改任务的执行周期
            CronTrigger trigger = new CronTrigger(CronConstant.MAIN_RESOURCE_LOAD);
            Date nextExec = trigger.nextExecutionTime(triggerContext);
            return nextExec;
        });
    }
}
