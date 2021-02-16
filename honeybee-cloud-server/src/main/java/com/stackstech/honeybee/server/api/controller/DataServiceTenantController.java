package com.stackstech.honeybee.server.api.controller;

import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.common.vo.PageQuery;
import com.stackstech.honeybee.server.api.entity.DataServiceAuthorityEntity;
import com.stackstech.honeybee.server.api.entity.DataServiceTenantEntity;
import com.stackstech.honeybee.server.api.service.TenantService;
import com.stackstech.honeybee.server.api.vo.DataAuthorityVo;
import com.stackstech.honeybee.server.api.vo.DataServiceTenantVo;
import com.stackstech.honeybee.server.api.vo.UpdateDataAuthorityMetaVo;
import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * data tenant service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@ApiResponses(@ApiResponse(code = 404, message = "data not found", response = ResponseObject.class))
@RestController
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class DataServiceTenantController {

    @Autowired
    private TenantService tenantService;

    @ApiOperation(value = "get data tenant")
    @RequestMapping(value = "/security/tenant/get/{id}", method = RequestMethod.GET)
    public ResponseObject get(@PathVariable("id") long id) {
        return ResponseObject.build().success(tenantService.getSingle(id));
    }

    @ApiOperation(value = "delete data tenant")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/security/tenant/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject delete(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(tenantService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update data tenant")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/tenant/update", method = RequestMethod.PUT)
    public ResponseObject update(@Validated({UpdateGroup.class}) @RequestBody DataServiceTenantVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceTenantEntity entity = new DataServiceTenantEntity().update(account.getId()).copy(vo);

        if (!tenantService.update(entity)) {
            return ResponseObject.build().failed("update tenant failed.");
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "add data tenant")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/security/tenant/add", method = RequestMethod.PUT)
    public ResponseObject add(@Validated({AddGroup.class}) @RequestBody DataServiceTenantVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceTenantEntity entity = new DataServiceTenantEntity().build(account.getId()).copy(vo);

        if (!tenantService.add(entity)) {
            return ResponseObject.build().failed("insert tenant failed.");
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "query data tenant")
    @RequestMapping(value = "/security/tenant/query", method = RequestMethod.POST)
    public ResponseObject query(@Validated @RequestBody PageQuery parameters) {
        List<DataServiceTenantEntity> data = tenantService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = tenantService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseObject.build().success(data, total);
        }
        return ResponseObject.build().failed("nothing found");
    }

    @ApiOperation(value = "get data authority")
    @RequestMapping(value = "/security/tenant/authority/{id}", method = RequestMethod.GET)
    public ResponseObject authority(@PathVariable("id") long id) {
        List<DataServiceAuthorityEntity> data = tenantService.getAuthorityList(id);
        if (data != null && data.size() > 0) {
            log.debug("query data record size {}", data.size());
            return ResponseObject.build().success(data);
        }
        return ResponseObject.build().failed("nothing found");
    }

    @ApiOperation(value = "query data authority meta")
    @RequestMapping(value = "/security/tenant/authority/meta", method = RequestMethod.POST)
    public ResponseObject getAuthorityMeta(@Validated @RequestBody DataAuthorityVo vo) {
        JsonParameterList metas = tenantService.getDataAuthorityMeta(vo.getId(), vo.getDataServiceId());
        return ResponseObject.build().success(metas);
    }

    @ApiOperation(value = "update data authority meta")
    @RequestMapping(value = "/security/tenant/authority/update", method = RequestMethod.PUT)
    public ResponseObject updateAuthorityMeta(@Validated @RequestBody UpdateDataAuthorityMetaVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(tenantService.updateDataAuthorityMeta(vo.getId(), vo.getDataAuthorityMeta(), account.getId()));
    }

    @ApiOperation(value = "delete data authority")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/security/tenant/authority/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject deleteAuthority(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(tenantService.deleteDataAuthority(id, account.getId()));
    }

    @ApiOperation(value = "add data authority")
    @RequestMapping(value = "/security/tenant/authority/add", method = RequestMethod.PUT)
    public ResponseObject addAuthorityMeta(@Validated @RequestBody DataAuthorityVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataServiceAuthorityEntity entity = tenantService.addDataAuthority(vo.getId(), vo.getDataServiceId(), account.getId());
        if (entity != null) {
            return ResponseObject.build().success(entity);
        }
        return ResponseObject.build().failed("add data authority failed");
    }


}
