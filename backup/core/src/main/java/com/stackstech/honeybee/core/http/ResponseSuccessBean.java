package com.stackstech.honeybee.core.http;

import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 返回请求成功之后的实体
 */
@XmlRootElement
@Component
public class ResponseSuccessBean extends ResponseBaseBean {
    //响应成功数据
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
