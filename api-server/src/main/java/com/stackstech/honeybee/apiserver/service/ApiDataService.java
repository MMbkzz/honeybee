package com.stackstech.honeybee.apiserver.service;


import com.stackstech.honeybee.apiserver.model.RequestData;

public interface ApiDataService {

    /**
     * 验证请求数据有效性
     *
     * @param requestData
     * @return
     */
    boolean valid(RequestData requestData);

    Object process(RequestData requestData);

    /**
     * 执行驱动
     *
     * @param requestData
     * @return
     */
    Object execute(RequestData requestData);

    /**
     * 拉取数据
     *
     * @param requestData
     * @return
     */
    Object get(RequestData requestData);

    /**
     * 推送数据
     *
     * @param requestData
     */
    void push(RequestData requestData);

}
