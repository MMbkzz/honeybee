package com.stackstech.dcp.apiserver.model;

import lombok.Data;

/**
 * API服务字段
 */
@Data
public class ServiceField {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段类型
     */
    private String type;


}
