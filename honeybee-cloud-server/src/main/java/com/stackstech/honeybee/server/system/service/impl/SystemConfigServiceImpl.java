package com.stackstech.honeybee.server.system.service.impl;

import com.stackstech.honeybee.server.core.enums.SysConfigMap;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.system.dao.SysConfigMapper;
import com.stackstech.honeybee.server.system.entity.SysConfigEntity;
import com.stackstech.honeybee.server.system.service.SystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    private SysConfigMapper mapper;

    @Override
    public String getSysConfig() throws ServerException, DataNotFoundException {
        SysConfigEntity entity = mapper.selectByConfigKey(SysConfigMap.APP_HONEYBEE_SERVER_CONFIG);
        if (entity != null && StringUtils.isNotEmpty(entity.getConfigValue())) {
            return entity.getConfigValue();
        }
        return null;
    }

    @Override
    public boolean updateSysConfig(String config) throws ServerException {
        return mapper.updateByConfigKey(SysConfigMap.APP_HONEYBEE_SERVER_CONFIG, config) > 0;
    }
}
