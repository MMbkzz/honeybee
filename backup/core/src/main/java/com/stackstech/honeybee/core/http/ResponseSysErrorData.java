package com.stackstech.honeybee.core.http;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ResponseSysErrorData implements Serializable {
    private static final long serialVersionUID = 1L;

    //descr about some error message
    private String message;
    //exception type
    private String type;
    //error  code
    private int code;
    //trace id,for debug log,may be a entity id or some property string
    @JsonProperty("trace_id")
    private String traceId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

}
