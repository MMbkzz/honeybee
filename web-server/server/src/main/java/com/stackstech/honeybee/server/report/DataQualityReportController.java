package com.stackstech.honeybee.server.report;

import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * DataServiceController
 *
 * @author william
 */
//TODO GYF
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@ApiResponses(@ApiResponse(code = 404, message = "data not found", response = ResponseMap.class))
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class DataQualityReportController {

    @RequestMapping(value = "/quality/report/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/quality/report/query", method = RequestMethod.POST)
    public ResponseMap<?> query(@RequestBody RequestParameter parameters) {
        return null;
    }

}
