package com.stackstech.dcp.core.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 请求错误时候返回的结构
 */
//添加此处注解为了controller层返回xml类型数据
@XmlRootElement
@Component
public class ResponseErrorData extends ResponseSysErrorData implements Serializable {

    @JsonProperty("error_subcode")
    private int errorSubcode;

    public int getErrorSubcode() {
        return errorSubcode;
    }

    public void setErrorSubcode(int errorSubcode) {
        this.errorSubcode = errorSubcode;
    }

}
