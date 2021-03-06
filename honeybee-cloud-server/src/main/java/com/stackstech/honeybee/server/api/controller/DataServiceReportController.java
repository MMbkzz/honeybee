package com.stackstech.honeybee.server.api.controller;

import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.common.vo.PageQuery;
import com.stackstech.honeybee.server.core.enums.Constant;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseObject get(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/data/service/report/query", method = RequestMethod.POST)
    public ResponseObject query(@Validated @RequestBody PageQuery parameters) {
        return null;
    }

}
