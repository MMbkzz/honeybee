package com.stackstech.honeybee.server.system.service;

import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;

public interface SystemConfigService {

    String getSysConfig() throws ServerException, DataNotFoundException;

    boolean updateSysConfig(String config) throws ServerException;
}
