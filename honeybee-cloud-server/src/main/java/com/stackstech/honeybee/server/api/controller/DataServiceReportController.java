package com.stackstech.honeybee.server.api.controller;

import com.stackstech.honeybee.common.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.common.vo.PageQuery;
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
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
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
