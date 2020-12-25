package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.core.cache.InstanceHeartbeatCache;
import com.stackstech.honeybee.core.constants.SysConfigConstant;
import com.stackstech.honeybee.core.enums.InstanceStageEnum;
import com.stackstech.honeybee.core.enums.InstanceStatusEnum;
import com.stackstech.honeybee.core.enums.TaskStatusEnum;
import com.stackstech.honeybee.server.platform.dao.InstanceMapper;
import com.stackstech.honeybee.server.platform.model.Instance;
import com.stackstech.honeybee.server.service.AuditLogService;
import com.stackstech.honeybee.server.service.HealthCheckService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private InstanceHeartbeatCache instanceHeartbeatCache;

    @Autowired
    private InstanceMapper instanceMapper;


    private final String[] stages = {
            InstanceStageEnum.initialized.code
            , InstanceStageEnum.registered.code
            , InstanceStageEnum.online.code
    };

    private final String[] statuses = {
            InstanceStatusEnum.unknown.code
            , InstanceStatusEnum.normal.code
    };


    @Override
    public void check() {

        // 当“实例状态 = ‘正常’and 心跳间隔大于未知阈值” 设置“实例状态 -> 未知”
        // 当“实例状态 = ‘未知’and 心跳间隔小于未知阈值” 设置“实例状态 -> 正常”
        // 当“实例状态 = ‘未知’and 心跳间隔大于待停止阈值” 设置“实例状态 -> 待停止”

        List<Instance> instances = instanceMapper.queryByStatus(statuses, stages);
        if (CollectionUtils.isEmpty(instances)) {
            // 记录日志
            auditLogService.logMessage("No valid instance found");
            return;
        }

        for (Instance instance : instances) {
            this.check(instance);
        }
    }

    @Override
    public void check(Instance instance) {
        Long now = System.currentTimeMillis();
        if (null == instance) {
            return;
        }

        Long beatTime = instanceHeartbeatCache.get(null, instance.getHost() + ":" + instance.getPort());
        // 未收到心跳包 设置“实例状态 -> 未知”
        if (null == beatTime) {
            instance.setStatusCode(InstanceStatusEnum.unknown.code);

            // 记录日志
            auditLogService.logMessage(instance.getHost() + ":" + instance.getPort() + "未查询到心跳，状态设置为" + InstanceStatusEnum.unknown.desc);
        } else if (now - beatTime <= SysConfigConstant.INSTANCE_HEARTBEAT_UNKNOW_TIMEOUT * 1000) {
            // 心跳间隔小于未知阈值” 设置“实例状态 -> 正常”
            instance.setStatusCode(InstanceStatusEnum.normal.code);

            // 记录日志
            auditLogService.logMessage(instance.getHost() + ":" + instance.getPort() + "心跳间隔小于未知阈值，状态设置为" + InstanceStatusEnum.normal.desc);
        } else if (now - beatTime >= SysConfigConstant.INSTANCE_HEARTBEAT_DECOMMISION_TIMEOUT * 1000) {
            // 心跳间隔大于待停止阈值 设置“实例状态 -> 待停止”
            instance.setStatusCode(InstanceStatusEnum.stopping.code);
            // 待停止节点， 删除心跳数据
            instanceHeartbeatCache.delete(null, instance.getHost() + ":" + instance.getPort());

            // 记录日志
            auditLogService.logStatus(TaskStatusEnum.err.code);
            auditLogService.logMessage(instance.getHost() + ":" + instance.getPort() + "心跳间隔大于待停止阈值，状态设置为" + InstanceStatusEnum.stopping.desc);
        } else if (now - beatTime >= SysConfigConstant.INSTANCE_HEARTBEAT_UNKNOW_TIMEOUT * 1000) {
            // 心跳间隔大于未知阈值 设置“实例状态 -> 未知”
            instance.setStatusCode(InstanceStatusEnum.unknown.code);

            // 记录日志
            auditLogService.logMessage(instance.getHost() + ":" + instance.getPort() + "心跳间隔大于未知阈值，状态设置为" + InstanceStatusEnum.unknown.desc);
        } else {
            instance.setStatusCode(InstanceStatusEnum.unknown.code);

            // 记录日志
            auditLogService.logMessage(instance.getHost() + ":" + instance.getPort() + "，状态设置为" + InstanceStatusEnum.unknown.desc);
        }

        instanceMapper.update(instance);
    }
}
