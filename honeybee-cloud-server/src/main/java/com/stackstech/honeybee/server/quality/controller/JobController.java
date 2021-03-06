package com.stackstech.honeybee.server.quality.controller;

import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.common.vo.PageQuery;
import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;
import com.stackstech.honeybee.server.core.service.BaseDataService;
import com.stackstech.honeybee.server.quality.entity.QualityJobEntity;
import com.stackstech.honeybee.server.quality.vo.QualityJobVo;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * data quality service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class JobController {

    @Autowired
    private BaseDataService<QualityJobEntity> service;


    @ApiOperation(value = "get quality job")
    @RequestMapping(value = "/quality/job/get/{id}", method = RequestMethod.GET)
    public ResponseObject getQualityJob(@PathVariable("id") long id) {
        return ResponseObject.build().success(service.getSingle(id));
    }

    @ApiOperation(value = "delete quality job")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/quality/job/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject deleteQualityJob(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(service.delete(id, account.getId()));
    }

    @ApiOperation(value = "update quality job")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/quality/job/update", method = RequestMethod.PUT)
    public ResponseObject updateQualityJob(@Validated({UpdateGroup.class}) @RequestBody QualityJobVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        QualityJobEntity entity = new QualityJobEntity().update(account.getId()).copy(vo);

        if (!service.update(entity)) {
            return ResponseObject.build().failed("data.update.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "add quality job")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/quality/job/add", method = RequestMethod.PUT)
    public ResponseObject addQualityJob(@Validated({AddGroup.class}) @RequestBody QualityJobVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        QualityJobEntity entity = new QualityJobEntity().build(account.getId()).copy(vo);

        if (!service.add(entity)) {
            return ResponseObject.build().failed("data.insert.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "query quality job")
    @RequestMapping(value = "/quality/job/query", method = RequestMethod.POST)
    public ResponseObject queryQualityJob(@Validated @RequestBody PageQuery parameters) {
        List<QualityJobEntity> data = service.get(parameters.getParameter());
        int total = service.getTotalCount(parameters.getParameter());
        return ResponseObject.build().success(data, total);
    }
}
