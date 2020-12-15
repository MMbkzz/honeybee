package com.stackstech.dcp.server.dataservice.vo;

import com.stackstech.dcp.server.dataasset.model.ServiceModel;
import com.stackstech.dcp.server.dataasset.model.ServiceModelField;
import com.stackstech.dcp.server.dataasset.model.ServiceModelParam;
import com.stackstech.dcp.server.dataservice.model.DataService;

import java.util.List;
import java.util.Map;

/**
 * 数据服务VO类
 */
public class DataServiceVO extends DataService {

    private String url;                             //访问URL

    private Map<String, Object> dataAssetArea;       //资产领域

    private Map<String, Object> dataAssetTopic;      //资产主题

    private ServiceModel serviceModel;              //服务模型

    private List<ServiceModelField> modelParams;    //参数列表

    private List<ServiceModelParam> modelFilters;   //过滤列表

    private List<AppUserVO> appUsers;               //app用户对象

    private String dataFormat;                      //返回数据格式<json/string>

    public Map<String, Object> getDataAssetArea() {
        return dataAssetArea;
    }

    public void setDataAssetArea(Map<String, Object> dataAssetArea) {
        this.dataAssetArea = dataAssetArea;
    }

    public Map<String, Object> getDataAssetTopic() {
        return dataAssetTopic;
    }

    public void setDataAssetTopic(Map<String, Object> dataAssetTopic) {
        this.dataAssetTopic = dataAssetTopic;
    }

    public ServiceModel getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(ServiceModel serviceModel) {
        this.serviceModel = serviceModel;
    }

    public List<AppUserVO> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(List<AppUserVO> appUsers) {
        this.appUsers = appUsers;
    }

    public List<ServiceModelField> getModelParams() {
        return modelParams;
    }

    public void setModelParams(List<ServiceModelField> modelParams) {
        this.modelParams = modelParams;
    }

    public List<ServiceModelParam> getModelFilters() {
        return modelFilters;
    }

    public void setModelFilters(List<ServiceModelParam> modelFilters) {
        this.modelFilters = modelFilters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }
}
