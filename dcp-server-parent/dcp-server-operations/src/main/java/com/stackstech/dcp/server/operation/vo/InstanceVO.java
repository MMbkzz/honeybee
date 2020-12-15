package com.stackstech.dcp.server.operation.vo;

import com.stackstech.dcp.server.datasource.model.ServiceSource;
import com.stackstech.dcp.server.platform.model.Instance;

import java.util.List;

/**
 * 资源实例VO
 */
public class InstanceVO extends Instance {

    private List<ServiceSource> serviceSources;           //数据源列表

    public List<ServiceSource> getServiceSources() {
        return serviceSources;
    }

    public void setServiceSources(List<ServiceSource> serviceSources) {
        this.serviceSources = serviceSources;
    }
}
