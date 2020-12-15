package com.stackstech.dcp.server.task;

import com.stackstech.dcp.core.enums.TaskEnum;
import com.stackstech.dcp.core.enums.TaskStatusEnum;
import com.stackstech.dcp.server.service.AuditLogService;
import com.stackstech.dcp.server.service.HealthCheckService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 心跳包线程， 负责向主数据存储当前实例心跳数据
 */
@Component
public class HealthCheckTask implements Runnable {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HealthCheckService healthCheckService;

    @Autowired
    private AuditLogService auditLogService;

    /**
     * 当“实例状态 = ‘正常’and 心跳间隔大于未知阈值” 设置“实例状态 -> 未知”
     * 当“实例状态 = ‘未知’and 心跳间隔小于未知阈值” 设置“实例状态 -> 正常”
     * 当“实例状态 = ‘未知’and 心跳间隔大于待停止阈值” 设置“实例状态 -> 待停止”
     */
    @Override
    public void run() {
        // 记录日志
        auditLogService.logBegin(TaskEnum.masterHeartbeat.toString());

        try {
            // 执行任务
            healthCheckService.check();
        } catch (Exception e) {
            logger.error("主数据任务 - 心跳检查异常", e);
            auditLogService.logStatus(TaskStatusEnum.err.code);
            auditLogService.logContext(ExceptionUtils.getFullStackTrace(e));
        }

        // 记录日志
        auditLogService.logEnd();
        // 记录日志
        auditLogService.insertLogs();
    }

}
