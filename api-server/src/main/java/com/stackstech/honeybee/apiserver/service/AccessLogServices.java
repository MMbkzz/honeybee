package com.stackstech.honeybee.apiserver.service;


import com.stackstech.honeybee.apiserver.model.RequestData;

import java.util.Map;

/**
 * api访问日志接口类
 */
public interface AccessLogServices {

    void save(Map<String, String> map, RequestData requestData);

    void insertLogs();
}
