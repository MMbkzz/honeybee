package com.stackstech.honeybee.server.system;

import com.stackstech.honeybee.server.core.entity.SysConfigEntity;
import com.stackstech.honeybee.server.core.enums.SysConfigMap;
import com.stackstech.honeybee.server.core.mapper.SysConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    private SysConfigMapper mapper;

    @Override
    public String getSysConfig() {
        SysConfigEntity entity = mapper.selectByConfigKey(SysConfigMap.APP_HONEYBEE_SERVER_CONFIG);
        if (entity != null && StringUtils.isNotEmpty(entity.getConfigValue())) {
            return entity.getConfigValue();
        }
        return null;
    }

    @Override
    public Boolean updateSysConfig(String config) {
        //TODO check config yaml code style
        return mapper.updateByConfigKey(SysConfigMap.APP_HONEYBEE_SERVER_CONFIG, config) > 0;
    }
}
