package com.stackstech.honeybee.server.system.controller;

import com.stackstech.honeybee.server.apiserver.service.DataService;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemController
 *
 * @author william
 */
//TODO WJ
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class SystemController {

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/system/config/get")
    public ResponseMap<?> getConfig() {
        return ResponseMap.success(dataService.getDataService(1L));
    }

    @PostMapping(value = "/system/config/update")
    public ResponseMap<?> updateConfig() {
        return null;
    }

    @PostMapping(value = "/system/license/get")
    public ResponseMap<?> getLicense() {
        return null;
    }

    @PostMapping(value = "/system/license/update")
    public ResponseMap<?> updateLicense() {
        return null;
    }


}
