package com.stackstech.honeybee.data.controller;

import com.stackstech.honeybee.data.core.enums.ApiEndpoint;
import com.stackstech.honeybee.data.entity.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value = "Data service api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class DataServiceController {

    @ApiOperation(value = "query data service")
    @RequestMapping(value = "/data/service/query", method = RequestMethod.POST)
    public ResponseEntity query(@Valid @RequestBody PageQuery parameters) {
        return ResponseEntity.ok("success");
    }
}
