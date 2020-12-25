package com.stackstech.honeybee.server.operation.vo;

import com.stackstech.honeybee.server.datasource.model.ServiceSource;
import com.stackstech.honeybee.server.platform.model.Instance;

import java.util.List;

/**
 * 数据源监控VO
 */
public class SourceMonitorVO extends ServiceSource {

    private List<Instance> instances;             //资源VO列表

    public List<Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }
}
