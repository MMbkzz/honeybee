package com.stackstech.honeybee.server.dataservice.vo;

import com.stackstech.honeybee.server.dataservice.model.AppUser;

import java.util.List;

/**
 * APP用户VO类
 */
public class UserVO extends AppUser {

    private List<AppServiceVO> abilityServices;//能力授权服务

    private List<AppServiceVO> appServices;   //资产授权服务

    public List<AppServiceVO> getAppServices() {
        return appServices;
    }

    public void setAppServices(List<AppServiceVO> appServices) {
        this.appServices = appServices;
    }

    public List<AppServiceVO> getAbilityServices() {
        return abilityServices;
    }

    public void setAbilityServices(List<AppServiceVO> abilityServices) {
        this.abilityServices = abilityServices;
    }

}
