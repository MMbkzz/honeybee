package com.stackstech.honeybee.server.system.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.server.core.annotation.*;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.SysConfigMap;
import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.types.DataSourceType;
import com.stackstech.honeybee.server.core.enums.types.EnumTypeMapping;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import com.stackstech.honeybee.server.system.entity.DataCacheEntity;
import com.stackstech.honeybee.server.system.entity.DataSourceEntity;
import com.stackstech.honeybee.server.system.service.DataCacheService;
import com.stackstech.honeybee.server.system.service.DataSourceService;
import com.stackstech.honeybee.server.system.service.SystemConfigService;
import com.stackstech.honeybee.server.system.vo.DataCacheQuery;
import com.stackstech.honeybee.server.system.vo.DataSourceQuery;
import com.stackstech.honeybee.server.system.vo.DataSourceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SystemController
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class SystemController {

    @Autowired
    private SystemConfigService service;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private DataCacheService dataCacheService;


    @ApiOperation(value = "get data source db config")
    @RequestMapping(value = "/system/datasource/conf/{dataSourceType}", method = RequestMethod.GET)
    public ResponseObject getDataSourceConfig(@PathVariable("dataSourceType") String dataSourceType) {
        DataSourceType type;
        try {
            type = DataSourceType.valueOf(dataSourceType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseObject.build().failed("datasource.type.invalid").of();
        }
        return ResponseObject.build().success(dataSourceService.getDataSourceConfig(type));
    }

    @ApiOperation(value = "get data source")
    @RequestMapping(value = "/system/datasource/get/{id}", method = RequestMethod.GET)
    public ResponseObject getDataSource(@PathVariable("id") long id) {
        return ResponseObject.build().success(dataSourceService.getSingle(id));
    }

    @ApiOperation(value = "delete data source")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/system/datasource/delete/{id}", method = RequestMethod.DELETE)
    public ResponseObject deleteDataSource(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(dataSourceService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update data source")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/system/datasource/update", method = RequestMethod.PUT)
    public ResponseObject updateDataSource(@Validated({UpdateGroup.class}) @RequestBody DataSourceVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataSourceEntity entity = new DataSourceEntity().update(account.getId()).copy(vo);

        if (!dataSourceService.update(entity)) {
            return ResponseObject.build().failed("data.update.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "add data source")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/system/datasource/add", method = RequestMethod.PUT)
    public ResponseObject addDataSource(@Validated({AddGroup.class}) @RequestBody DataSourceVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataSourceEntity entity = new DataSourceEntity().build(account.getId()).copy(vo);

        if (!dataSourceService.add(entity)) {
            return ResponseObject.build().failed("data.insert.failed").of();
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "query data source")
    @RequestMapping(value = "/system/datasource/query", method = RequestMethod.POST)
    public ResponseObject queryDataSource(@Validated @RequestBody DataSourceQuery parameters) {
        List<DataSourceEntity> data = dataSourceService.get(parameters.getParameter());
        int total = dataSourceService.getTotalCount(parameters.getParameter());
        return ResponseObject.build().success(data, total);
    }

    @Deprecated
    @ApiOperation(value = "testing api for data cache")
    @RequestMapping(value = "/system/datacache/add", method = RequestMethod.GET)
    public ResponseObject test() {
        List<String> result = Lists.newArrayList();
        result.add(RandomStringUtils.randomAlphanumeric(22));

        for (int i = 0; i < 66; i++) {
            DataCacheEntity entity = new DataCacheEntity();
            entity.setUuid(String.valueOf((i + 1)));
            entity.setData(result);
            entity.setExpire(300);
            entity.setUpdatetime(new Date());
            dataCacheService.addDataCache(entity);
        }
        return ResponseObject.build().success(true);
    }

    @ApiOperation(value = "get data cache")
    @RequestMapping(value = "/system/datacache/get/{uuid}", method = RequestMethod.GET)
    public ResponseObject getDataCache(@PathVariable("uuid") String uuid) {
        return ResponseObject.build().success(dataCacheService.getDataCache(uuid));
    }

    @ApiOperation(value = "delete data cache")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/system/datacache/delete/{uuid}", method = RequestMethod.DELETE)
    public ResponseObject deleteDataCache(@PathVariable("uuid") String uuid, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseObject.build().success(dataCacheService.delete(uuid));
    }

    @ApiOperation(value = "query data cache")
    @RequestMapping(value = "/system/datacache/query", method = RequestMethod.POST)
    public ResponseObject queryDataCache(@Validated @RequestBody DataCacheQuery parameters) {
        List<DataCacheEntity> data = dataCacheService.get(parameters.getKeywords(), parameters.getPageStart(), parameters.getPageSize());
        int total = dataCacheService.getTotalCount(parameters.getKeywords());
        return ResponseObject.build().success(data, total);
    }

    @ApiOperation(value = "get system config")
    @RequestMapping(value = "/system/config/get", method = RequestMethod.GET)
    public ResponseObject getConfig() {
        String configValue = service.getSysConfig();
        if (StringUtils.isNotEmpty(configValue)) {
            Map<String, String> map = Maps.newHashMap();
            map.put(SysConfigMap.APP_HONEYBEE_SERVER_CONFIG, configValue);
            return ResponseObject.build().success(map);
        }
        return ResponseObject.build().failed("config.value.empty").of();
    }

    @ApiOperation(value = "update system config")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/system/config/update", method = RequestMethod.PUT)
    public ResponseObject updateConfig(@Validated @NotBlank(message = "config cannot be null") @RequestBody String config) {
        if (StringUtils.isNotEmpty(config)) {
            //TODO check config yaml code style
            boolean flag = service.updateSysConfig(config);
            if (flag) {
                //TODO flush nacos global config
                return ResponseObject.build().success(true);
            }
        }
        return ResponseObject.build().failed("data.update.failed").of();
    }

    @ApiOperation(value = "get system license")
    @PostMapping(value = "/system/license/get")
    public ResponseObject getLicense() {
        //TODO
        return null;
    }

    @ApiOperation(value = "update system license")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @PostMapping(value = "/system/license/update")
    public ResponseObject updateLicense(@Validated @NotBlank(message = "license cannot be null") @RequestBody String license) {
        //TODO
        return null;
    }

    @ApiAuthIgnore
    @ApiOperation(value = "get global dict mapping")
    @RequestMapping(value = "/system/dict/get", method = RequestMethod.GET)
    public ResponseObject getDict() {
        return ResponseObject.build().success(EnumTypeMapping.getMapping());
    }

}
