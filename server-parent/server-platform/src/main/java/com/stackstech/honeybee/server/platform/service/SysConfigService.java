package com.stackstech.honeybee.server.platform.service;

import com.stackstech.honeybee.server.dataasset.model.ModelCode;
import com.stackstech.honeybee.server.platform.model.SysConfig;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface SysConfigService {

    List<Map<String, Object>> queryAll(ModelCode modelCode) throws Exception;

    Map<String, Object> queryConfig();

    int delete(Long id) throws Exception;

    int update(SysConfig sysConfig) throws Exception;

    int add(SysConfig sysConfig) throws Exception;

    List<SysConfig> queryAll(SysConfig sysConfig);

}
