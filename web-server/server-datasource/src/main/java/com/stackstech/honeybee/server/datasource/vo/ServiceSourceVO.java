package com.stackstech.honeybee.server.datasource.vo;

import com.stackstech.honeybee.server.datasource.model.ServiceSource;
import com.stackstech.honeybee.server.param.model.Parameter;
import com.stackstech.honeybee.server.platform.model.ServiceDriver;

import java.io.Serializable;
import java.util.List;

/**
 * DataSource参数配置VO
 */
public class ServiceSourceVO extends ServiceSource implements Serializable {

    private static final long serialVersionUID = -7521655802612940677L;

    //公有配置参数列表
    private List<Parameter> paramConfigList;

    //数据驱动
    private ServiceDriver serviceDriver;

    public List<Parameter> getParamConfigList() {
        return paramConfigList;
    }

    public void setParamConfigList(List<Parameter> paramConfigList) {
        this.paramConfigList = paramConfigList;
    }

    public ServiceDriver getServiceDriver() {
        return serviceDriver;
    }

    public void setServiceDriver(ServiceDriver serviceDriver) {
        this.serviceDriver = serviceDriver;
    }
}
