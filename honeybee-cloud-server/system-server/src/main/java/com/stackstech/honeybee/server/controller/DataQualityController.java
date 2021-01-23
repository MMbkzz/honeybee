package com.stackstech.honeybee.server.controller;

import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.entity.AccountEntity;
import com.stackstech.honeybee.server.core.entity.QualityJobEntity;
import com.stackstech.honeybee.server.core.entity.QualityRuleEntity;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.vo.PageQuery;
import com.stackstech.honeybee.server.core.vo.QualityJobVo;
import com.stackstech.honeybee.server.core.vo.QualityRuleVo;
import com.stackstech.honeybee.server.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
public class DataQualityController {

    @Autowired
    private DataService<QualityJobVo, QualityJobEntity> qualityJobService;
    @Autowired
    private DataService<QualityRuleVo, QualityRuleEntity> qualityRuleService;


    @ApiOperation(value = "get quality job")
    @RequestMapping(value = "/quality/job/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getQualityJob(@PathVariable("id") long id) {
        return ResponseMap.success(qualityJobService.getSingle(id));
    }

    @ApiOperation(value = "delete quality job")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/quality/job/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteQualityJob(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(qualityJobService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update quality job")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/quality/job/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateQualityJob(@Valid @RequestBody QualityJobVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        if (!qualityJobService.update(vo, account.getId())) {
            return ResponseMap.failed("update data quality job failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add quality job")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/quality/job/add", method = RequestMethod.PUT)
    public ResponseMap<?> addQualityJob(@Valid @RequestBody QualityJobVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        if (!qualityJobService.add(vo, account.getId())) {
            return ResponseMap.failed("insert data quality job failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "query quality job")
    @RequestMapping(value = "/quality/job/query", method = RequestMethod.POST)
    public ResponseMap<?> queryQualityJob(@Valid @RequestBody PageQuery parameters) {
        List<QualityJobEntity> data = qualityJobService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = qualityJobService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @ApiOperation(value = "get quality rule")
    @RequestMapping(value = "/quality/rule/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getQualityRule(@PathVariable("id") long id) {
        return ResponseMap.success(qualityRuleService.getSingle(id));
    }

    @ApiOperation(value = "delete quality rule")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/quality/rule/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteQualityRule(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(qualityRuleService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update quality rule")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/quality/rule/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateQualityRule(@Valid @RequestBody QualityRuleVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        if (!qualityRuleService.update(vo, account.getId())) {
            return ResponseMap.failed("update data quality rule failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add quality rule")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/quality/rule/add", method = RequestMethod.PUT)
    public ResponseMap<?> addQualityRule(@Valid @RequestBody QualityRuleVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        if (!qualityRuleService.add(vo, account.getId())) {
            return ResponseMap.failed("insert data quality rule failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "query quality rule")
    @RequestMapping(value = "/quality/rule/query", method = RequestMethod.POST)
    public ResponseMap<?> queryQualityRule(@Valid @RequestBody PageQuery parameters) {
        List<QualityRuleEntity> data = qualityRuleService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = qualityRuleService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

}
