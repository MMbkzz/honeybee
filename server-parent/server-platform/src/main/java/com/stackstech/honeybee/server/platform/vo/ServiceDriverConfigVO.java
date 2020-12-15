package com.stackstech.honeybee.server.platform.vo;

import com.stackstech.honeybee.server.param.model.Parameter;
import com.stackstech.honeybee.server.platform.model.ServiceDriver;

import java.io.Serializable;
import java.util.List;

/**
 * 驱动管理-参数配置VO类
 */
public class ServiceDriverConfigVO extends ServiceDriver implements Serializable {

    private static final long serialVersionUID = -4054205426628833627L;

    private List<Parameter> paramConfigList;

    public List<Parameter> getParamConfigList() {
        return paramConfigList;
    }

    public void setParamConfigList(List<Parameter> paramConfigList) {
        this.paramConfigList = paramConfigList;
    }
}
