package com.stackstech.dcp.apiserver.service.impl;

import com.google.common.collect.Maps;
import com.stackstech.dcp.apiserver.conf.ServerConfig;
import com.stackstech.dcp.apiserver.health.InstanceResourceCheck;
import com.stackstech.dcp.apiserver.service.AuditLogService;
import com.stackstech.dcp.apiserver.service.InstanceOfflineService;
import com.stackstech.dcp.apiserver.service.ResourceService;
import com.stackstech.dcp.connector.core.ResourceSessionFactory;
import com.stackstech.dcp.connector.core.ResourceSessionManager;
import com.stackstech.dcp.core.cache.ResourceActiveCache;
import com.stackstech.dcp.core.cache.ResourceExpectCache;
import com.stackstech.dcp.core.cache.ResourceExpectStatusCache;
import com.stackstech.dcp.core.cache.ResourceHoldCache;
import com.stackstech.dcp.core.enums.ResourceLoadStatusEnum;
import com.stackstech.dcp.core.enums.TaskStatusEnum;
import com.stackstech.dcp.server.datasource.dao.ServiceSourceMapper;
import com.stackstech.dcp.server.datasource.model.ServiceSource;
import com.stackstech.dcp.server.param.dao.ParameterMapper;
import com.stackstech.dcp.server.platform.dao.InstanceMapper;
import com.stackstech.dcp.server.platform.dao.InstanceResourceMapper;
import com.stackstech.dcp.server.platform.dao.ServiceDriverMapper;
import com.stackstech.dcp.server.platform.model.Instance;
import com.stackstech.dcp.server.platform.model.ServiceDriver;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ResourceSessionManager resourceSessionManager;

    @Autowired
    private ResourceExpectCache resourceExpectCache;

    @Autowired
    private ResourceHoldCache resourceHoldCache;

    @Autowired
    private ResourceExpectStatusCache resourceExpectStatusCache;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ServiceSourceMapper serviceSourceMapper;

    @Autowired
    private ServiceDriverMapper serviceDriverMapper;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private InstanceResourceCheck instanceResourceCheck;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private InstanceMapper instanceMapper;

    @Autowired
    private InstanceResourceMapper instanceResourceMapper;
    @Autowired
    private InstanceOfflineService instanceOfflineService;

    @Autowired
    private ResourceActiveCache resourceActiveCache;

    @Override
    public Map<String, Integer> getLocalResource() {
        Map<String, ResourceSessionFactory> resourceSessionFactoryMap = resourceSessionManager.getResource();
        if (null == resourceSessionFactoryMap || resourceSessionFactoryMap.isEmpty()) {
            return Maps.newHashMap();
        }

        Map<String, Integer> resource = new HashMap<>();
        resourceSessionFactoryMap.keySet().forEach(resourceId -> {
            try {
                resource.put(resourceId, resourceSessionFactoryMap.get(resourceId).getPoolSize());
            } catch (Exception e) {
                resource.put(resourceId, 0);
            }
        });
        return resource;
    }

    @Override
    public Map<String, Integer> getExpectResource() {
        return resourceExpectCache.get(serverConfig.getReal());
    }

    @Override
    public void saveHoldResource(String resourceId, Integer poolSize) {
        resourceHoldCache.put(serverConfig.getReal(), resourceId, poolSize);

        logger.warn("预期资源状态:" + resourceId + " @ " + serverConfig.getReal() + " @ " + ResourceLoadStatusEnum.done.code);
        resourceExpectStatusCache.put(resourceId, serverConfig.getReal(), ResourceLoadStatusEnum.done.toString());
    }

    @Override
    public void saveHoldResource(Map<String, Integer> resource) {
        if (MapUtils.isEmpty(resource)) {
            return;
        }
        resourceHoldCache.put(serverConfig.getReal(), resource);
    }

    @Override
    public void resetPoolSize(String resourceId, Integer poolSize) {
        resourceSessionManager.getResource(resourceId).resetPoolSize(poolSize);
    }

    @Override
    public void initResource(String resourceId) {
        if (StringUtils.isBlank(resourceId)) {
            return;
        }

        ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(resourceId);
        ServiceDriver serviceDriver = serviceDriverMapper.queryByPrimaryKey(serviceSource.getDriverId());

        String jarFile = serviceDriver.getDriverPath();
        String mainClass = serviceDriver.getDriverClass();
        Map<String, Object> properties = getProperties(resourceId);
        resourceSessionManager.openSession(resourceId
                , properties
                , jarFile
                , mainClass);
    }

    @Override
    public void removeResource(String resourceId) {
        // 关闭资源
        try {
            resourceSessionManager.close(resourceId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.warn("预期资源状态:" + resourceId + " @ " + serverConfig.getReal() + " @ " + ResourceLoadStatusEnum.stopped.code);
        resourceExpectStatusCache.put(resourceId, serverConfig.getReal(), ResourceLoadStatusEnum.stopped.code);

        // 释放持有资源数
        resourceHoldCache.delete(serverConfig.getReal(), resourceId);
        // 释放期望资源数
        resourceExpectCache.delete(serverConfig.getReal(), resourceId);
        // 释放实例资源表
        Instance instance = instanceMapper.queryByHost(serverConfig.getId(), serverConfig.getHost(), String.valueOf(serverConfig.getPort()));
        if (null != instance) {
            instanceResourceMapper.delete(instance.getId(), resourceId);
        }
    }

    /**
     * 监听主数据分配的预期资源数
     * 1. 发现预期资源数与持有资源数不一致时进行资源申请或释放；
     * 2. 当“预期资源数 = 持有资源  = 0 and 状态 =‘待停止’”， 设置实例状态为“停止”，并退出实例
     */
    @Override
    public void configResource() {
        Map<String, Integer> expect = this.getExpectResource();

        Map<String, Integer> hold = this.getLocalResource();

        if (null == expect || expect.isEmpty()) {
            // 记录日志
            auditLogService.logMessage("期望资源为空");
            // 没有预期资源也执行下线逻辑。 防止集群实例运行中时， 下线了所有数据源， 再关闭节点无法自动触发
            instanceOfflineService.load();
            return;
        }

        expect.forEach((key, value) -> {
            try {
                configExpectResource(key, value, hold);
            } catch (Exception e) {
                logger.error("节点任务 - 资源初始化异常", e);

                auditLogService.logStatus(TaskStatusEnum.err.code);
                auditLogService.logContext(ExceptionUtils.getFullStackTrace(e));
                auditLogService.logMessage("资源：" + key + "，初始化异常");
            }
        });

        // 资源初始化后， 将实例状态置为有效
        instanceResourceCheck.up();
        // 判断实例是否需要下线
        instanceOfflineService.load();
    }

    @Override
    public void configExpectResource(String resourceId, Integer expectPoolSize, Map<String, Integer> hold) {
        if (StringUtils.isBlank(resourceId)) {
            return;
        }

        // 获取当前实例{资源}分配执行状态
//        if (ResourceLoadStatusEnum.done.toString().equals(resourceExpectStatusCache.get(resourceId, serverConfig.getReal()))) {
//            return;
//        }

        // 1. 当“预期资源数 = 持有资源  = 0 and 状态 =‘待停止’”， 设置实例状态为“停止”，并退出实例
        if (null == expectPoolSize || 0 == expectPoolSize) {
            this.removeResource(resourceId);

            // 记录日志
            auditLogService.logMessage("删除资源：" + resourceId);
            return;
        }

        // 2. 发现预期资源数与持有资源数不一致时进行资源申请
//        if (expectPoolSize.equals(resourceHoldCache.get(serverConfig.getReal(), resourceId))) {
//            return;
//        }
        ResourceSessionFactory sessionFactory = resourceSessionManager.getResource(resourceId);
        if (null != sessionFactory && expectPoolSize.equals(sessionFactory.getPoolSize())) {
            return;
        }

        Integer holdPoolSize = hold.get(resourceId);
        if (null == holdPoolSize) {
            this.initResource(resourceId);
        }

        if (null == holdPoolSize || 0 != expectPoolSize.compareTo(holdPoolSize)) {
            this.resetPoolSize(resourceId, expectPoolSize);
        }

        // 记录日志
        auditLogService.logMessage("资源：" + resourceId + "，分配数量：" + expectPoolSize);

        this.saveHoldResource(resourceId, expectPoolSize);
    }

    @Override
    public void saveActiveResource(String resourceId, Integer poolSize) {
        resourceActiveCache.put(serverConfig.getReal(), resourceId, poolSize);
    }


    private Map<String, Object> getProperties(String resourceId) {
        Map<String, Object> params = parameterMapper.queryParams("DATASOURCE", resourceId);
        return Maps.transformEntries(params, (s, value) -> {
            if (value != null) {
                return ((Map) value).get("param_value");
            }
            return null;
        });
    }
}
