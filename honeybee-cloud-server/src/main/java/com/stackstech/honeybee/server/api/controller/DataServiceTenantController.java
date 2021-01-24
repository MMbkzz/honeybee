package com.stackstech.honeybee.server.api.controller;

import com.stackstech.honeybee.server.api.entity.DataServiceTenantEntity;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.common.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.api.vo.DataServiceTenantVo;
import com.stackstech.honeybee.server.common.vo.PageQuery;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * data tenant service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@ApiResponses(@ApiResponse(code = 404, message = "data not found", response = ResponseMap.class))
@RestController
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class DataServiceTenantController {

    @Autowired
    private DataService<DataServiceTenantEntity> tenantService;

    @RequestMapping(value = "/security/tenant/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return ResponseMap.success(tenantService.getSingle(id));
    }

    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/security/tenant/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> delete(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(tenantService.delete(id, account.getId()));
    }

    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/tenant/update", method = RequestMethod.PUT)
    public ResponseMap<?> update(@Valid @RequestBody DataServiceTenantVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceTenantEntity entity = new DataServiceTenantEntity().update(account.getId());
        BeanUtils.copyProperties(vo, entity);

        if (!tenantService.update(entity)) {
            return ResponseMap.failed("update tenant failed.");
        }
        return ResponseMap.success(true);
    }

    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/security/tenant/add", method = RequestMethod.PUT)
    public ResponseMap<?> add(@Valid @RequestBody DataServiceTenantVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceTenantEntity entity = new DataServiceTenantEntity().build(account.getId());
        BeanUtils.copyProperties(vo, entity);

        if (!tenantService.add(entity)) {
            return ResponseMap.failed("insert tenant failed.");
        }
        return ResponseMap.success(true);
    }

    @RequestMapping(value = "/security/tenant/query", method = RequestMethod.POST)
    public ResponseMap<?> query(@Valid @RequestBody PageQuery parameters) {
        List<DataServiceTenantEntity> data = tenantService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = tenantService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }


}
