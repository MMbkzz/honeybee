package com.stackstech.dcp.server.task;

import com.stackstech.dcp.core.enums.TaskEnum;
import com.stackstech.dcp.core.enums.TaskStatusEnum;
import com.stackstech.dcp.server.service.AuditLogService;
import com.stackstech.dcp.server.service.MasterDataService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 资源线程. 负责监听主数据分配的预期资源数
 */
@Component
public class ResourceLoadTask implements Runnable {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private MasterDataService masterDataService;

    @Override
    public void run() {
        // 记录日志
        auditLogService.logBegin(TaskEnum.masterResource.toString());

        //boolean error = false;
        //String errorMessage = null;
        try {
            // 执行任务
            // resourceLoadService.load();

            masterDataService.execute();
        } catch (Exception e) {
            logger.error("主数据任务 - 资源分配异常", e);

            auditLogService.logStatus(TaskStatusEnum.err.code);
            auditLogService.logContext(ExceptionUtils.getFullStackTrace(e));
        }

        // 记录日志
        auditLogService.logEnd();
        // 记录日志
        auditLogService.insertLogs();
    }

}
