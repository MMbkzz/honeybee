package com.stackstech.honeybee.server.audit;

import com.stackstech.honeybee.server.core.entity.AuditLogEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DataServiceController
 *
 * @author william
 */
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class AuditController {

    private final Logger log = LoggerFactory.getLogger(AuditController.class);

    @Autowired
    private DataService<AuditLogEntity> service;

    @RequestMapping(value = "/audit/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return ResponseMap.success(service.getSingle(id));
    }

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
