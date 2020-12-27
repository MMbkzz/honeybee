package com.stackstech.honeybee.server.apiserver.controller;

import com.stackstech.honeybee.server.apiserver.service.DataService;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DataServiceController
 * @author william
 */
//TODO WJ
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class DataServiceController {

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/data/service/get")
    public ResponseMap<?> get() {
        return ResponseMap.success(dataService.getDataService(1L));
    }

    @PostMapping(value = "/data/service/delete")
    public ResponseMap<?> delete() {
        return null;
    }

    @PostMapping(value = "/data/service/update")
    public ResponseMap<?> update() {
        return null;
    }

    @PostMapping(value = "/data/service/add")
    public ResponseMap<?> add() {
        return null;
    }

    @PostMapping(value = "/data/service/query")
    public ResponseMap<?> query() {
        return null;
    }

    @PostMapping(value = "/data/service/online")
    public ResponseMap<?> online() {
        return null;
    }

    @PostMapping(value = "/data/service/offline")
    public ResponseMap<?> offline() {
        return null;
    }

}
