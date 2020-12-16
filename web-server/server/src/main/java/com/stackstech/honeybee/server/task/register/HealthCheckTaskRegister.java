package com.stackstech.honeybee.server.task.register;

import com.stackstech.honeybee.core.constants.CronConstant;
import com.stackstech.honeybee.server.task.HealthCheckTask;
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
public class HealthCheckTaskRegister implements SchedulingConfigurer {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HealthCheckTask heartbeatTask;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(heartbeatTask, triggerContext -> {
            // 任务触发，可修改任务的执行周期
            CronTrigger trigger = new CronTrigger(CronConstant.MAIN_HEALTH_CHECK);
            Date nextExec = trigger.nextExecutionTime(triggerContext);
            return nextExec;
        });
    }
}
