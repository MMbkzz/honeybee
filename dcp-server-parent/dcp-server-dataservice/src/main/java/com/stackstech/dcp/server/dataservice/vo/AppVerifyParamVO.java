package com.stackstech.dcp.server.dataservice.vo;

import java.util.List;
import java.util.Map;

/**
 * 授权验证参数VO类
 */
public class AppVerifyParamVO {

    private List<Map<String, Object>> fields;                    //字段列表

    private List<Map<String, Object>> params;                   //过滤列表

    public List<Map<String, Object>> getFields() {
        return fields;
    }

    public void setFields(List<Map<String, Object>> fields) {
        this.fields = fields;
    }

    public List<Map<String, Object>> getParams() {
        return params;
    }

    public void setParams(List<Map<String, Object>> params) {
        this.params = params;
    }
}
