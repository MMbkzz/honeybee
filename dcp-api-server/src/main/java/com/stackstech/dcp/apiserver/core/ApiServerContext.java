package com.stackstech.dcp.apiserver.core;

import com.stackstech.dcp.apiserver.conf.ServerConfig;
import com.stackstech.dcp.core.conf.ApplicationConfig;
import com.stackstech.dcp.core.enums.InstanceStageEnum;
import com.stackstech.dcp.core.enums.InstanceStatusEnum;
import com.stackstech.dcp.server.platform.model.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApiServerContext {

    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    private ConfigurableApplicationContext applicationContext;
    @Autowired
    private ServerConfig serverConfig;

    private Instance serverInstance;

    private InstanceStageEnum serverStage;

    private InstanceStatusEnum serverStatus;


    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getServerId() {
        return serverConfig.getId();
    }

    public String getServerHost() {
        return serverConfig.getHost();
    }

    public Integer getServerPort() {
        return serverConfig.getPort();
    }

    public String getServerFullAddr() {
        return serverConfig.getReal();
    }

    public Instance getServerInstance() {
        return serverInstance;
    }

    public InstanceStageEnum getServerStage() {
        return serverStage;
    }

    public InstanceStatusEnum getServerStatus() {
        return serverStatus;
    }

    public void init(Instance serverInstance, InstanceStageEnum serverStage, InstanceStatusEnum serverStatus) {
        this.serverInstance = serverInstance;
        this.serverStage = serverStage;
        this.serverStatus = serverStatus;
    }


}
