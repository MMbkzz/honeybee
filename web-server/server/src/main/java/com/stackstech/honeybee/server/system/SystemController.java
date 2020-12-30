package com.stackstech.honeybee.server.system;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.enums.SysConfigMap;
import com.stackstech.honeybee.server.system.SystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SystemController
 *
 * @author william
 */
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class SystemController {

    @Autowired
    private SystemConfigService service;

    @RequestMapping(value = "/system/config/get", method = RequestMethod.GET)
    public ResponseMap<?> getConfig() {
        String configValue = service.getSysConfig();
        if (StringUtils.isNotEmpty(configValue)) {
            Map<String, String> map = Maps.newHashMap();
            map.put(SysConfigMap.APP_HONEYBEE_SERVER_CONFIG, configValue);
            return ResponseMap.success(map);
        }
        return ResponseMap.failed("config value is empty.");
    }

    @RequestMapping(value = "/system/config/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateConfig(@RequestBody String config) {
        if (StringUtils.isNotEmpty(config)) {
            return ResponseMap.success(service.updateSysConfig(config));
        }
        return ResponseMap.failed("update system config failed.");
    }

    @PostMapping(value = "/system/license/get")
    public ResponseMap<?> getLicense() {
        //TODO
        return null;
    }

    @PostMapping(value = "/system/license/update")
    public ResponseMap<?> updateLicense() {
        //TODO
        return null;
    }


}
