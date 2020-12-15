package com.stackstech.dcp.server.dataasset.vo;

import com.stackstech.dcp.server.dataasset.model.ServiceModel;
import com.stackstech.dcp.server.dataasset.model.ServiceModelField;
import com.stackstech.dcp.server.dataasset.model.ServiceModelParam;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 资产模型工具类VO
 */
public class ServiceModelVO extends ServiceModel implements Serializable {

    //父模型名称
    private String parentModelName;

    //数据资产领域
    private Map<String, Object> dataAssetArea;

    //数据资产主题
    private Map<String, Object> dataAssetTopic;

    //数据源
    private Map<String, Object> serviceSource;

    //字段列表
    private List<ServiceModelField> serviceModelFields;

    //参数列表
    private List<ServiceModelParam> serviceModelParams;

    //字段同步标志
    private boolean fieldLoad;


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

    public Map<String, Object> getServiceSource() {
        return serviceSource;
    }

    public void setServiceSource(Map<String, Object> serviceSource) {
        this.serviceSource = serviceSource;
    }

    public List<ServiceModelField> getServiceModelFields() {
        return serviceModelFields;
    }

    public void setServiceModelFields(List<ServiceModelField> serviceModelFields) {
        this.serviceModelFields = serviceModelFields;
    }

    public List<ServiceModelParam> getServiceModelParams() {
        return serviceModelParams;
    }

    public void setServiceModelParams(List<ServiceModelParam> serviceModelParams) {
        this.serviceModelParams = serviceModelParams;
    }

    public String getParentModelName() {
        return parentModelName;
    }

    public void setParentModelName(String parentModelName) {
        this.parentModelName = parentModelName;
    }

    public boolean isFieldLoad() {
        return fieldLoad;
    }

    public void setFieldLoad(boolean fieldLoad) {
        this.fieldLoad = fieldLoad;
    }
}
