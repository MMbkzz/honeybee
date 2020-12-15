package com.stackstech.dcp.apiserver.health;

import com.google.common.collect.Maps;
import com.stackstech.dcp.apiserver.conf.ServerConfig;
import com.stackstech.dcp.connector.core.ResourceSessionFactory;
import com.stackstech.dcp.connector.core.ResourceSessionManager;
import com.stackstech.dcp.core.cache.InstanceOnlineCache;
import com.stackstech.dcp.core.enums.InstanceStageEnum;
import com.stackstech.dcp.server.platform.dao.InstanceMapper;
import com.stackstech.dcp.server.platform.model.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InstanceResourceCheck implements HealthIndicator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 实例资源状态
     * <p>
     * 0:不可用， 1：可用
     */
    private static int INSTANTCE_RESOURCE_STATUS = 0;

    @Autowired
    private InstanceOnlineCache instanceOnlineCache;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private InstanceMapper instanceMapper;

    @Autowired
    private ResourceSessionManager resourceSessionManager;

    @Override
    public Health health() {
        if (0 == INSTANTCE_RESOURCE_STATUS) {
            return Health.down().withDetail("status", "down").build();
        }

        Map<String, ResourceSessionFactory> resourceSessionFactory = resourceSessionManager.getResource();

        Map<String, Integer> resources = Maps.transformEntries(resourceSessionFactory, (s, sessionFactory) -> {
            try {
                if (sessionFactory != null) {
                    return sessionFactory.getPoolSize();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        });
        return Health.up().withDetail("resource pool", resources).build();
    }

    public synchronized void up() {
        if (INSTANTCE_RESOURCE_STATUS == 1) {
            return;
        }

        // 实例状态.  已激活 -> 已初始化
        Instance instance = instanceMapper.queryByHost(serverConfig.getId(), serverConfig.getHost(), String.valueOf(serverConfig.getPort()));
        instance.setStageCode(InstanceStageEnum.initialized.toString());
        instanceMapper.update(instance);

        // 实例健康状态 - UP
        INSTANTCE_RESOURCE_STATUS = 1;

        // 上线缓存 - 删除
        instanceOnlineCache.delete(null, serverConfig.getReal());

        // 实例状态 -> 上线
        instance.setStageCode(InstanceStageEnum.online.toString());
        instanceMapper.update(instance);
    }

    public synchronized void down() {
        if (INSTANTCE_RESOURCE_STATUS != 0) {
            INSTANTCE_RESOURCE_STATUS = 0;
        }
    }
}
