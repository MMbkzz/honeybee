package com.stackstech.honeybee.server.system.service;

import com.stackstech.honeybee.server.core.entity.SysConfigEntity;

public interface SystemConfigService {

    String getSysConfig();

    Boolean updateSysConfig(String config);
}
