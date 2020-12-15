package com.stackstech.honeybee.apiserver.service.impl;

import com.stackstech.honeybee.apiserver.core.ApiServerContext;
import com.stackstech.honeybee.apiserver.exception.ApiServerException;
import com.stackstech.honeybee.apiserver.health.InstanceResourceCheck;
import com.stackstech.honeybee.apiserver.service.LifeCycleService;
import com.stackstech.honeybee.connector.core.ResourceSessionManager;
import com.stackstech.honeybee.core.cache.InstanceHeartbeatCache;
import com.stackstech.honeybee.core.cache.InstanceOfflineCache;
import com.stackstech.honeybee.core.cache.InstanceOnlineCache;
import com.stackstech.honeybee.core.cache.ResourceExpectStatusCache;
import com.stackstech.honeybee.core.enums.InstanceStageEnum;
import com.stackstech.honeybee.core.enums.InstanceStatusEnum;
import com.stackstech.honeybee.server.platform.dao.InstanceMapper;
import com.stackstech.honeybee.server.platform.model.Instance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class LifeCycleServiceImpl implements LifeCycleService, ApplicationContextAware {

    @Autowired
    private ApiServerContext apiServerContext;
    @Autowired
    private InstanceMapper instanceMapper;
    //////////
    @Autowired
    private InstanceResourceCheck instanceResourceCheck;
    @Autowired
    private ResourceSessionManager resourceSessionManager;
    @Autowired
    private InstanceHeartbeatCache instanceHeartbeatCache;
    @Autowired
    private ResourceExpectStatusCache resourceExpectStatusCache;
    @Autowired
    private InstanceOnlineCache instanceOnlineCache;
    @Autowired
    private InstanceOfflineCache instanceOfflineCache;


    @PostConstruct
    @Override
    public void startup() {
        Instance instance = instanceMapper.queryByHost(apiServerContext.getServerId(), apiServerContext.getServerHost(), String.valueOf(apiServerContext.getServerPort()));
        if (instance == null) {
            instance = register();
        }
        apiServerContext.init(instance, InstanceStageEnum.valueOf(instance.getStageCode()), InstanceStatusEnum.valueOf(instance.getStatusCode()));
    }

    @Override
    public Instance register() {
        boolean flag = false;
        Instance instance = new Instance();
        String id = Optional.ofNullable(apiServerContext.getServerId()).orElse(UUID.randomUUID().toString());
        instance.setId(id);
        instance.setName(StringUtils.join("API-", id));
        instance.setHost(apiServerContext.getServerHost());
        instance.setPort(String.valueOf(apiServerContext.getServerPort()));
        instance.setHostUser("unknow"); //TODO remove this filed in the table
        instance.setHostPassword("unknow"); //TODO remove this filed in the table
        instance.setInstancePath("/"); //TODO remove this filed in the table
        instance.setStatusCode(InstanceStatusEnum.normal.code);
        instance.setStageCode(InstanceStageEnum.unactivated.code);
        instance.setCreateBy(-1L);
        instance.setUpdateBy(-1L); //TODO remove this filed in the table
        flag = instanceMapper.insert(instance) > 0;
        if (!flag) {
            throw new ApiServerException("Server registration failed, Please check the log for more information.");
        }
        return instance;
    }

    @Override
    public void shotdown() {
        Instance instance = apiServerContext.getServerInstance();
        if (null == instance && !InstanceStatusEnum.stopping.code.equals(instance.getStatusCode())) {
            return;
        }

        // step1. 取消注册
        unregister(instance);

        // step2. 释放资源
        dealloc(instance);

        // step3. 下线关闭应用
        close(instance);
    }

    private void unregister(Instance instance) {
        instance.setStageCode(InstanceStageEnum.unregister.code);
        instanceMapper.update(instance);

        instanceResourceCheck.down();
    }

    private void dealloc(Instance instance) {
        instance.setStageCode(InstanceStageEnum.dealloc.code);
        instanceMapper.update(instance);

        deallocStatusCache();

        // 释放数据源
        resourceSessionManager.closeAll();
    }

    private void close(Instance instance) {
        instance.setStatusCode(InstanceStatusEnum.stopped.code);
        instance.setStageCode(InstanceStageEnum.stopped.code);
        instanceMapper.update(instance);

        if (null != apiServerContext.getApplicationContext()) {
            apiServerContext.getApplicationContext().close();
        }
    }

    private void deallocStatusCache() {
        String key = apiServerContext.getServerFullAddr();
        // 释放缓存
        // 释放缓存 - 心跳包
        instanceHeartbeatCache.delete(null, key);
        // 释放缓存 -
        instanceOnlineCache.delete(null, key);
        // 释放缓存 -
        instanceOfflineCache.delete(null, key);
        // 释放缓存 - 预期资源执行状态
        resourceExpectStatusCache.delete(null, key);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            apiServerContext.setApplicationContext((ConfigurableApplicationContext) applicationContext);
        }
    }
}
