package com.stackstech.honeybee.server.audit.controller;

import com.stackstech.honeybee.common.entity.ResponseMap;
import com.stackstech.honeybee.server.audit.entity.AuditLogEntity;
import com.stackstech.honeybee.server.audit.vo.AuditLogQuery;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.service.BaseDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * audit log service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditController {

    @Autowired
    private BaseDataService<AuditLogEntity> service;

    @ApiOperation(value = "get audit log")
    @RequestMapping(value = "/audit/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return ResponseMap.success(service.getSingle(id));
    }

    @ApiOperation(value = "query audit log")
    @RequestMapping(value = "/audit/{auditType}/query", method = RequestMethod.POST)
    public ResponseMap<?> query(@PathVariable("auditType") String auditType, @Validated @RequestBody AuditLogQuery parameters) {
        Map<String, Object> params = parameters.getParameter();
        params.put("auditType", auditType);
        List<AuditLogEntity> data = service.get(params);
        if (data != null && data.size() > 0) {
            int total = service.getTotalCount(params);
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

}
