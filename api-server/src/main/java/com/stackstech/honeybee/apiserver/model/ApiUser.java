package com.stackstech.honeybee.apiserver.model;

import lombok.Data;

/**
 * API用户信息
 */
@Data
public class ApiUser {

    /**
     * 用户token
     */
    private String token;

    /**
     * api service code
     */
    private String apiCode;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 请求参数列表
     */
    private ServiceParam params;


}
