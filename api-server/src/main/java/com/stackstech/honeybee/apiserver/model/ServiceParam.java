package com.stackstech.honeybee.apiserver.model;

import lombok.Data;

import java.util.List;

/**
 * 服务参数
 */
@Data
public class ServiceParam {

    private List<ServiceField> fields;

    private List<ServiceFilter> params;

}
