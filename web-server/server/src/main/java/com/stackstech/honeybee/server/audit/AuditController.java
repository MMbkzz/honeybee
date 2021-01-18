package com.stackstech.honeybee.server.audit;

import com.stackstech.honeybee.server.core.entity.AuditLogEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * audit log service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditController {

    @Autowired
    private DataService<AuditLogEntity> service;

    @ApiOperation(value = "get audit log")
    @RequestMapping(value = "/audit/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return ResponseMap.success(service.getSingle(id));
    }

    @ApiOperation(value = "query audit log")
    @RequestMapping(value = "/audit/{auditType}/query", method = RequestMethod.POST)
    public ResponseMap<?> query(@PathVariable("auditType") String auditType, @RequestBody RequestParameter parameters) {
        List<AuditLogEntity> data = service.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = service.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

}
