package com.stackstech.honeybee.server.report.controller;

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
//TODO GYF
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class DataQualityReportController {

    @Autowired
    private DataService dataService;

    @PostMapping(value = "/quality/report/get")
    public ResponseMap<?> get() {
        return ResponseMap.success(dataService.getDataService(1L));
    }

    @PostMapping(value = "/quality/report/query")
    public ResponseMap<?> query() {
        return null;
    }

}
