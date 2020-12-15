package com.stackstech.honeybee.apiserver.model;

import lombok.Data;

@Data
public class RequestData {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 数据服务ID
     */
    private String dataServiceId;

    /**
     * 数据服务访问令牌
     */
    private String token;

    /**
     * 接收消息信息
     */
    private String message;

    /**
     * 数据集
     */
    private Object body;

    /**
     * 请求参数列表
     */
    private ServiceParam data;


}
