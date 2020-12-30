package com.stackstech.honeybee.server.report;

import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import org.springframework.web.bind.annotation.*;

/**
 * DataServiceController
 *
 * @author william
 */
//TODO GYF
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class DataServiceReportController {

    @RequestMapping(value = "/data/service/report/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/data/service/report/query", method = RequestMethod.POST)
    public ResponseMap<?> query(@RequestBody RequestParameter parameters) {
        return null;
    }

}
