package com.stackstech.honeybee.server.api.controller;

import com.stackstech.honeybee.common.entity.ResponseMap;
import com.stackstech.honeybee.common.vo.PageQuery;
import com.stackstech.honeybee.server.api.entity.DataAuthorityMeta;
import com.stackstech.honeybee.server.api.entity.DataServiceAuthorityEntity;
import com.stackstech.honeybee.server.api.entity.DataServiceTenantEntity;
import com.stackstech.honeybee.server.api.service.TenantService;
import com.stackstech.honeybee.server.api.vo.DataAuthorityVo;
import com.stackstech.honeybee.server.api.vo.DataServiceTenantVo;
import com.stackstech.honeybee.server.api.vo.UpdateDataAuthorityMetaVo;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
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
    private TenantService tenantService;

    @ApiOperation(value = "get data tenant")
    @RequestMapping(value = "/security/tenant/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return ResponseMap.success(tenantService.getSingle(id));
    }

    @ApiOperation(value = "delete data tenant")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/security/tenant/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> delete(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(tenantService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update data tenant")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/tenant/update", method = RequestMethod.PUT)
    public ResponseMap<?> update(@Valid @RequestBody DataServiceTenantVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceTenantEntity entity = new DataServiceTenantEntity().update(account.getId()).copy(vo);

        if (!tenantService.update(entity)) {
            return ResponseMap.failed("update tenant failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add data tenant")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/security/tenant/add", method = RequestMethod.PUT)
    public ResponseMap<?> add(@Valid @RequestBody DataServiceTenantVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceTenantEntity entity = new DataServiceTenantEntity().build(account.getId()).copy(vo);

        if (!tenantService.add(entity)) {
            return ResponseMap.failed("insert tenant failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "query data tenant")
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

    @ApiOperation(value = "get data authority")
    @RequestMapping(value = "/security/tenant/authority/{id}", method = RequestMethod.GET)
    public ResponseMap<?> authority(@PathVariable("id") long id) {
        List<DataServiceAuthorityEntity> data = tenantService.getAuthorityList(id);
        if (data != null && data.size() > 0) {
            log.debug("query data record size {}", data.size());
            return ResponseMap.success(data);
        }
        return ResponseMap.failed("nothing found");
    }

    @ApiOperation(value = "query data authority meta")
    @RequestMapping(value = "/security/tenant/authority/meta", method = RequestMethod.POST)
    public ResponseMap<?> getAuthorityMeta(@Valid @RequestBody DataAuthorityVo vo) {
        List<DataAuthorityMeta> metas = tenantService.getDataAuthorityMeta(vo.getId(), vo.getDataServiceId());
        return ResponseMap.success(metas);
    }

    @ApiOperation(value = "update data authority meta")
    @RequestMapping(value = "/security/tenant/authority/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateAuthorityMeta(@Valid @RequestBody UpdateDataAuthorityMetaVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(tenantService.updateDataAuthorityMeta(vo.getId(), vo.getDataAuthorityMeta(), account.getId()));
    }

    @ApiOperation(value = "delete data authority")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/security/tenant/authority/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteAuthority(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(tenantService.deleteDataAuthority(id, account.getId()));
    }

    @ApiOperation(value = "add data authority")
    @RequestMapping(value = "/security/tenant/authority/add", method = RequestMethod.PUT)
    public ResponseMap<?> addAuthorityMeta(@Valid @RequestBody DataAuthorityVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceAuthorityEntity entity = tenantService.addDataAuthority(vo.getId(), vo.getDataServiceId(), account.getId());
        if (entity != null) {
            return ResponseMap.success(entity);
        }
        return ResponseMap.failed("add data authority failed");
    }


}
