package com.stackstech.honeybee.apiserver.model;

import lombok.Data;

/**
 * 服务参数请求接口
 */
@Data
public class ServiceFilter {

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数值
     */
    private Object value;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 参数对应字段属性名(类型)
     */
    private String field;

}
