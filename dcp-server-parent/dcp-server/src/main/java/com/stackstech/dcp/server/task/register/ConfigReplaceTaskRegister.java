package com.stackstech.dcp.server.task.register;

import com.stackstech.dcp.server.task.ConfigReplaceTask;
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

/**
 * 定时更新配置
 */
@Lazy(false)
@Component
@EnableScheduling
public class ConfigReplaceTaskRegister implements SchedulingConfigurer {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String HEARTBEAT = "0/30 * * * * ?";
    @Autowired
    private ConfigReplaceTask configReplaceTask;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(configReplaceTask, triggerContext -> {
            // 任务触发，可修改任务的执行周期
            CronTrigger trigger = new CronTrigger(ConfigReplaceTaskRegister.HEARTBEAT);
            Date nextExec = trigger.nextExecutionTime(triggerContext);
            return nextExec;
        });
    }
}
