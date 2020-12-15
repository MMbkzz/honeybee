package com.stackstech.dcp.server.dataservice.vo;

import com.stackstech.dcp.server.dataservice.model.AccessLog;

public class AccessLogVO extends AccessLog {

    private String dataServiceName;
    private String name;

    public String getDataServiceName() {
        return dataServiceName;
    }

    public void setDataServiceName(String dataServiceName) {
        this.dataServiceName = dataServiceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
