package com.stackstech.honeybee.server.audit;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.core.entity.AuditLogEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.service.DataService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        Map<String, Object> params = Maps.newHashMap();
        params.put("pageStart", parameters.getPageStart());
        params.put("pageSize", parameters.getPageSize());
        if (StringUtils.isNotEmpty(parameters.getType())) {
            params.put("logType", parameters.getType().toUpperCase());
        }
        params.put("auditType", auditType.toUpperCase());
        params.put("keywords", parameters.getKeywords());
        params.put("order", parameters.getOrder());

        List<AuditLogEntity> data = service.get(params);
        if (data != null && data.size() > 0) {
            int total = service.getTotalCount(params);
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

}
