package com.stackstech.honeybee.server.quality.controller;

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
public class DataQualityRuleController {

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/quality/rule/get")
    public ResponseMap<?> get() {
        return ResponseMap.success(dataService.getDataService(1L));
    }

    @PostMapping(value = "/quality/rule/delete")
    public ResponseMap<?> delete() {
        return null;
    }

    @PostMapping(value = "/quality/rule/update")
    public ResponseMap<?> update() {
        return null;
    }

    @PostMapping(value = "/quality/rule/add")
    public ResponseMap<?> add() {
        return null;
    }

    @PostMapping(value = "/quality/rule/query")
    public ResponseMap<?> query() {
        return null;
    }

}