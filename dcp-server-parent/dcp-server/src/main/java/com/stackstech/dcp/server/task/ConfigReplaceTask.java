package com.stackstech.dcp.server.task;

import com.stackstech.dcp.core.constants.CronConstant;
import com.stackstech.dcp.core.constants.SysConfigConstant;
import com.stackstech.dcp.server.platform.model.SysConfig;
import com.stackstech.dcp.server.platform.service.impl.SysConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置更新线程
 */
@Component
public class ConfigReplaceTask implements Runnable {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysConfigServiceImpl sysConfigServiceImpl;

    @Override
    public void run() {

        List<SysConfig> sysConfigs = sysConfigServiceImpl.queryAll(new SysConfig());
        for (SysConfig sysConfig : sysConfigs) {
            if ("instance.heartbeat.unknow.timeout".equals(sysConfig.getConfigName().trim())) {

                SysConfigConstant.INSTANCE_HEARTBEAT_UNKNOW_TIMEOUT = Long.valueOf(sysConfig.getConfigValue());
            } else if ("instance.heartbeat.decommision.timeout".equals(sysConfig.getConfigName().trim())) {

                SysConfigConstant.INSTANCE_HEARTBEAT_DECOMMISION_TIMEOUT = Long.valueOf(sysConfig.getConfigValue());
            } else if ("NODE_HEARTBEAT".equals(sysConfig.getConfigName().trim())) {

                CronConstant.NODE_HEARTBEAT = sysConfig.getConfigValue().trim();
            } else if ("NODE_RESOURCE".equals(sysConfig.getConfigName().trim())) {

                CronConstant.NODE_RESOURCE = sysConfig.getConfigValue().trim();
            } else if ("NODE_INSTANCE_OFFLINE".equals(sysConfig.getConfigName().trim())) {

                CronConstant.NODE_INSTANCE_OFFLINE = sysConfig.getConfigValue().trim();
            } else if ("MAIN_HEALTH_CHECK".equals(sysConfig.getConfigName().trim())) {

                CronConstant.MAIN_HEALTH_CHECK = sysConfig.getConfigValue().trim();
            } else if ("MAIN_RESOURCE_LOAD".equals(sysConfig.getConfigName().trim())) {
                CronConstant.MAIN_RESOURCE_LOAD = sysConfig.getConfigValue().trim();
            }
        }
    }
}
