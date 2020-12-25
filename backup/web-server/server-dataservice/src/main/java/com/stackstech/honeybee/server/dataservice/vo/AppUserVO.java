package com.stackstech.honeybee.server.dataservice.vo;

import com.stackstech.honeybee.server.dataservice.model.AppDs;
import com.stackstech.honeybee.server.dataservice.model.AppDsField;
import com.stackstech.honeybee.server.dataservice.model.AppUser;

import java.util.List;

/**
 * 授权用户VO
 */
public class AppUserVO extends AppUser {

    private AppDs appDs;                       //授权<token>

    private List<AppDsField> dsFields;          //授权字段

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
}
