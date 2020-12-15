package com.stackstech.dcp.server.dataservice.vo;

import com.stackstech.dcp.server.dataasset.model.ServiceModelField;
import com.stackstech.dcp.server.dataservice.model.AppDs;
import com.stackstech.dcp.server.dataservice.model.AppDsField;
import com.stackstech.dcp.server.dataservice.model.DataService;

import java.util.List;

/**
 * 授权服务VO
 */
public class AppServiceVO extends DataService {

    private AppDs appDs;                         //授权AppDs

    private List<AppDsField> dsFields;           //授权字段

    private List<ServiceModelField> modelParams;    //参数列表

    public List<AppDsField> getDsFields() {
        return dsFields;
    }

    public void setDsFields(List<AppDsField> dsFields) {
        this.dsFields = dsFields;
    }

    public AppDs getAppDs() {
        return appDs;
    }

    public void setAppDs(AppDs appDs) {
        this.appDs = appDs;
    }

    public List<ServiceModelField> getModelParams() {
        return modelParams;
    }

    public void setModelParams(List<ServiceModelField> modelParams) {
        this.modelParams = modelParams;
    }
}
