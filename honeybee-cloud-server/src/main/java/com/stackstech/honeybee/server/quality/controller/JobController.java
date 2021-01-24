package com.stackstech.honeybee.server.quality.controller;

import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.common.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.common.vo.PageQuery;
import com.stackstech.honeybee.server.quality.vo.QualityJobVo;
import com.stackstech.honeybee.server.quality.entity.QualityJobEntity;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
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
    private DataService<QualityJobEntity> service;


    @ApiOperation(value = "get quality job")
    @RequestMapping(value = "/quality/job/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getQualityJob(@PathVariable("id") long id) {
        return ResponseMap.success(service.getSingle(id));
    }

    @ApiOperation(value = "delete quality job")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/quality/job/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteQualityJob(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(service.delete(id, account.getId()));
    }

    @ApiOperation(value = "update quality job")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/quality/job/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateQualityJob(@Valid @RequestBody QualityJobVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        QualityJobEntity entity = new QualityJobEntity().update(account.getId());
        BeanUtils.copyProperties(vo, entity);

        if (!service.update(entity)) {
            return ResponseMap.failed("update data quality job failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add quality job")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/quality/job/add", method = RequestMethod.PUT)
    public ResponseMap<?> addQualityJob(@Valid @RequestBody QualityJobVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        QualityJobEntity entity = new QualityJobEntity().build(account.getId());
        BeanUtils.copyProperties(vo, entity);

        if (!service.add(entity)) {
            return ResponseMap.failed("insert data quality job failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "query quality job")
    @RequestMapping(value = "/quality/job/query", method = RequestMethod.POST)
    public ResponseMap<?> queryQualityJob(@Valid @RequestBody PageQuery parameters) {
        List<QualityJobEntity> data = service.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = service.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }
}
