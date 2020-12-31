package com.stackstech.honeybee.server.quality;

import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.entity.*;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.core.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * data quality service controller
 *
 * @author william
 */
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class DataQualityController {

    private final Logger log = LoggerFactory.getLogger(DataQualityController.class);

    @Autowired
    private DataService<QualityJobEntity> qualityJobService;
    @Autowired
    private DataService<QualityRuleEntity> qualityRuleService;


    @RequestMapping(value = "/quality/job/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getQualityJob(@PathVariable("id") long id) {
        return ResponseMap.success(qualityJobService.getSingle(id));
    }

    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/quality/job/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteQualityJob(@PathVariable("id") long id) {
        return ResponseMap.success(qualityJobService.delete(id));
    }

    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/quality/job/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateQualityJob(@RequestBody QualityJobEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setUpdatetime(new Date());
        });
        if (!qualityJobService.update(entity)) {
            return ResponseMap.failed("update data quality job failed.");
        }
        return ResponseMap.success(true);
    }

    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/quality/job/add", method = RequestMethod.PUT)
    public ResponseMap<?> addQualityJob(@RequestBody QualityJobEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setId(null);
            entity.setStatus(EntityStatusType.ENABLE.getStatus());
            entity.setUpdatetime(new Date());
            entity.setCreatetime(new Date());
        });
        if (!qualityJobService.add(entity)) {
            return ResponseMap.failed("insert data quality job failed.");
        }
        return ResponseMap.success(entity);
    }

    @RequestMapping(value = "/quality/job/query", method = RequestMethod.POST)
    public ResponseMap<?> queryQualityJob(@RequestBody RequestParameter parameters) {
        List<QualityJobEntity> data = qualityJobService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = qualityJobService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @RequestMapping(value = "/quality/rule/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getQualityRule(@PathVariable("id") long id) {
        return ResponseMap.success(qualityRuleService.getSingle(id));
    }

    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/quality/rule/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteQualityRule(@PathVariable("id") long id) {
        return ResponseMap.success(qualityRuleService.delete(id));
    }

    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/quality/rule/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateQualityRule(@RequestBody QualityRuleEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setUpdatetime(new Date());
        });
        if (!qualityRuleService.update(entity)) {
            return ResponseMap.failed("update data quality rule failed.");
        }
        return ResponseMap.success(true);
    }

    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/quality/rule/add", method = RequestMethod.PUT)
    public ResponseMap<?> addQualityRule(@RequestBody QualityRuleEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setId(null);
            entity.setStatus(EntityStatusType.ENABLE.getStatus());
            entity.setUpdatetime(new Date());
            entity.setCreatetime(new Date());
        });
        if (!qualityRuleService.add(entity)) {
            return ResponseMap.failed("insert data quality rule failed.");
        }
        return ResponseMap.success(entity);
    }


    @RequestMapping(value = "/quality/rule/query", method = RequestMethod.POST)
    public ResponseMap<?> queryQualityRule(@RequestBody RequestParameter parameters) {
        List<QualityRuleEntity> data = qualityRuleService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = qualityRuleService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

}
