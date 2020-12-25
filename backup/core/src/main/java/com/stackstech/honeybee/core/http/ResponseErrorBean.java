package com.stackstech.honeybee.core.http;

import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 请求错误时候返回的实体
 */
//添加此处注解为了controller层返回xml类型数据
@XmlRootElement
@Component
public class ResponseErrorBean extends ResponseBaseBean {
    //error bean
    private ResponseSysErrorData error;

    public ResponseSysErrorData getError() {
        return error;
    }

    public void setError(ResponseSysErrorData error) {
        this.error = error;
    }

}
