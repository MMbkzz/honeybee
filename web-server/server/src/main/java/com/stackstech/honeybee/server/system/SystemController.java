package com.stackstech.honeybee.server.system;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.entity.DataSourceEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.core.enums.SysConfigMap;
import com.stackstech.honeybee.server.core.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SystemController
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class SystemController {

    @Autowired
    private SystemConfigService service;
    @Autowired
    private DataService<DataSourceEntity> dataSourceService;

    @ApiOperation(value = "get data source")
    @RequestMapping(value = "/system/datasource/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getDataSource(@PathVariable("id") long id) {
        return ResponseMap.success(dataSourceService.getSingle(id));
    }

    @ApiOperation(value = "delete data source")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/system/datasource/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteDataSource(@PathVariable("id") long id) {
        return ResponseMap.success(dataSourceService.delete(id));
    }

    @ApiOperation(value = "update data source")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/system/datasource/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateDataSource(@RequestBody DataSourceEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setUpdatetime(new Date());
        });
        if (!dataSourceService.update(entity)) {
            return ResponseMap.failed("update data source failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add data source")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/system/datasource/add", method = RequestMethod.PUT)
    public ResponseMap<?> addDataSource(@RequestBody DataSourceEntity entity) {
        Optional.ofNullable(entity).ifPresent(u -> {
            entity.setId(null);
            entity.setStatus(EntityStatusType.ENABLE.getStatus());
            entity.setUpdatetime(new Date());
            entity.setCreatetime(new Date());
        });
        if (!dataSourceService.add(entity)) {
            return ResponseMap.failed("insert data source failed.");
        }
        return ResponseMap.success(entity);
    }

    @ApiOperation(value = "query data source")
    @RequestMapping(value = "/system/datasource/query", method = RequestMethod.POST)
    public ResponseMap<?> queryDataSource(@RequestBody RequestParameter parameters) {
        List<DataSourceEntity> data = dataSourceService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = dataSourceService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @ApiOperation(value = "get data cache")
    @RequestMapping(value = "/system/datacache/get/{key}", method = RequestMethod.GET)
    public ResponseMap<?> getDataCache(@PathVariable("key") String key) {
        //TODO
        return null;
    }

    @ApiOperation(value = "delete data cache")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/system/datacache/delete/{key}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteDataCache(@PathVariable("key") String key) {
        //TODO
        return null;
    }

    @ApiOperation(value = "query data cache")
    @RequestMapping(value = "/system/datacache/query", method = RequestMethod.POST)
    public ResponseMap<?> queryDataCache(@RequestBody RequestParameter parameters) {
        //TODO
        return null;
    }

    @ApiOperation(value = "get system config")
    @RequestMapping(value = "/system/config/get", method = RequestMethod.GET)
    public ResponseMap<?> getConfig() {
        String configValue = service.getSysConfig();
        if (StringUtils.isNotEmpty(configValue)) {
            Map<String, String> map = Maps.newHashMap();
            map.put(SysConfigMap.APP_HONEYBEE_SERVER_CONFIG, configValue);
            return ResponseMap.success(map);
        }
        return ResponseMap.failed("config value is empty.");
    }

    @ApiOperation(value = "update system config")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/system/config/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateConfig(@RequestBody String config) {
        if (StringUtils.isNotEmpty(config)) {
            //TODO check config yaml code style
            boolean flag = service.updateSysConfig(config);
            if (flag) {
                //TODO flush nacos global config
                return ResponseMap.success(flag);
            }
        }
        return ResponseMap.failed("update system config failed.");
    }

    @ApiOperation(value = "get system license")
    @PostMapping(value = "/system/license/get")
    public ResponseMap<?> getLicense() {
        //TODO
        return null;
    }

    @ApiOperation(value = "update system license")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @PostMapping(value = "/system/license/update")
    public ResponseMap<?> updateLicense() {
        //TODO
        return null;
    }


}
