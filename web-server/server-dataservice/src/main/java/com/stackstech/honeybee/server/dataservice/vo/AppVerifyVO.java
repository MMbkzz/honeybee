package com.stackstech.honeybee.server.dataservice.vo;

/**
 * 授权验证VO类
 */
public class AppVerifyVO {

    private String token;                   //用户Token
    private String dataServiceId;           //数据服务Id
    private String appId;                   //服务用户Id
    private String dataFormat;              //返回数据格式<json/string>
    private AppVerifyParamVO data;          //验证参数

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDataServiceId() {
        return dataServiceId;
    }

    public void setDataServiceId(String dataServiceId) {
        this.dataServiceId = dataServiceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public AppVerifyParamVO getData() {
        return data;
    }

    public void setData(AppVerifyParamVO data) {
        this.data = data;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }
}
