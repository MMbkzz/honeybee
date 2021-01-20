package com.stackstech.honeybee.server.report;

import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.vo.PageQuery;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * DataServiceController
 *
 * @author william
 */
//TODO GYF
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class DataServiceReportController {

    @RequestMapping(value = "/data/service/report/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/data/service/report/query", method = RequestMethod.POST)
    public ResponseMap<?> query(@Valid @RequestBody PageQuery parameters) {
        return null;
    }

}
