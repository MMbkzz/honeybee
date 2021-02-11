package com.stackstech.honeybee.server.quality.controller;

import com.stackstech.honeybee.common.entity.ResponseMap;
import com.stackstech.honeybee.common.vo.PageQuery;
import com.stackstech.honeybee.server.core.enums.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
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
public class DataQualityReportController {

    @ApiOperation(value = "get quality report")
    @RequestMapping(value = "/quality/report/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return null;
    }

    @ApiOperation(value = "query quality report")
    @RequestMapping(value = "/quality/report/query", method = RequestMethod.POST)
    public ResponseMap<?> query(@Validated @RequestBody PageQuery parameters) {
        return null;
    }

}
