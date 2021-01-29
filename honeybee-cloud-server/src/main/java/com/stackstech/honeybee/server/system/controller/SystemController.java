package com.stackstech.honeybee.server.system.controller;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.stackstech.honeybee.common.entity.ResponseMap;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.annotation.ApiAuthIgnore;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.enums.*;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import com.stackstech.honeybee.server.system.entity.DataCacheEntity;
import com.stackstech.honeybee.server.system.entity.DataSourceEntity;
import com.stackstech.honeybee.server.system.entity.DictMapping;
import com.stackstech.honeybee.server.system.service.DataCacheService;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    private DataService<DataSourceEntity> dataSourceService;
    @Autowired
    private DataCacheService dataCacheService;

    @ApiOperation(value = "get data source")
    @RequestMapping(value = "/system/datasource/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getDataSource(@PathVariable("id") long id) {
        return ResponseMap.success(dataSourceService.getSingle(id));
    }

    @ApiOperation(value = "delete data source")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/system/datasource/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteDataSource(@PathVariable("id") long id, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(dataSourceService.delete(id, account.getId()));
    }

    @ApiOperation(value = "update data source")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/system/datasource/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateDataSource(@Valid @RequestBody DataSourceVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataSourceEntity entity = new DataSourceEntity().update(account.getId()).copy(vo);
        entity.setDatasourceConfig(CommonUtil.toJsonString(vo.getDatasourceParameters()));

        if (!dataSourceService.update(entity)) {
            return ResponseMap.failed("update data source failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "add data source")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.INSERT)
    @RequestMapping(value = "/system/datasource/add", method = RequestMethod.PUT)
    public ResponseMap<?> addDataSource(@Valid @RequestBody DataSourceVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        DataSourceEntity entity = new DataSourceEntity().build(account.getId()).copy(vo);
        entity.setDatasourceConfig(CommonUtil.toJsonString(vo.getDatasourceParameters()));

        if (!dataSourceService.add(entity)) {
            return ResponseMap.failed("insert data source failed.");
        }
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "query data source")
    @RequestMapping(value = "/system/datasource/query", method = RequestMethod.POST)
    public ResponseMap<?> queryDataSource(@Valid @RequestBody DataSourceQuery parameters) {
        List<DataSourceEntity> data = dataSourceService.get(parameters.getParameter());
        if (data != null && data.size() > 0) {
            int total = dataSourceService.getTotalCount(parameters.getParameter());
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @Deprecated
    @ApiOperation(value = "testing api for data cache")
    @RequestMapping(value = "/system/datacache/add", method = RequestMethod.GET)
    public ResponseMap<?> test() {
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
        return ResponseMap.success(true);
    }

    @ApiOperation(value = "get data cache")
    @RequestMapping(value = "/system/datacache/get/{uuid}", method = RequestMethod.GET)
    public ResponseMap<?> getDataCache(@PathVariable("uuid") String uuid) {
        return ResponseMap.success(dataCacheService.getDataCache(uuid));
    }

    @ApiOperation(value = "delete data cache")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.DELETE)
    @RequestMapping(value = "/system/datacache/delete/{uuid}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteDataCache(@PathVariable("uuid") String uuid, @ApiIgnore @RequestAccount AccountEntity account) {
        return ResponseMap.success(dataCacheService.delete(uuid));
    }

    @ApiOperation(value = "query data cache")
    @RequestMapping(value = "/system/datacache/query", method = RequestMethod.POST)
    public ResponseMap<?> queryDataCache(@Valid @RequestBody DataCacheQuery parameters) {
        List<DataCacheEntity> data = dataCacheService.get(parameters.getPageStart(), parameters.getPageSize());
        if (data != null && data.size() > 0) {
            int total = dataCacheService.getTotalCount();
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
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
    public ResponseMap<?> updateConfig(@NotNull(message = "config cannot be null") @RequestBody String config) {
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
    public ResponseMap<?> updateLicense(@NotNull(message = "license cannot be null") @RequestBody String license) {
        //TODO
        return null;
    }

    @ApiAuthIgnore
    @ApiOperation(value = "get global dict mapping")
    @RequestMapping(value = "/system/dict/get", method = RequestMethod.GET)
    public ResponseMap<?> getDataSource() {
        Map<String, List<DictMapping>> mapping = Maps.newHashMap();

        List<DictMapping> statusMapping = Lists.newArrayList();
        statusMapping.add(new DictMapping().build(EntityStatusType.ENABLE.getStatus(), EntityStatusType.ENABLE.getDesc()));
        statusMapping.add(new DictMapping().build(EntityStatusType.DISABLE.getStatus(), EntityStatusType.DISABLE.getDesc()));
        mapping.put("STATUS", statusMapping);

        List<DictMapping> serviceStatusMapping = Lists.newArrayList();
        serviceStatusMapping.add(new DictMapping().build(EntityStatusType.ENABLE.getStatus(), EntityStatusType.ENABLE.getDesc()));
        serviceStatusMapping.add(new DictMapping().build(EntityStatusType.DISABLE.getStatus(), EntityStatusType.DISABLE.getDesc()));
        serviceStatusMapping.add(new DictMapping().build(EntityStatusType.OFFLINE.getStatus(), EntityStatusType.OFFLINE.getDesc()));
        serviceStatusMapping.add(new DictMapping().build(EntityStatusType.ONLINE.getStatus(), EntityStatusType.ONLINE.getDesc()));
        mapping.put("SERVICE_STATUS", serviceStatusMapping);

        List<DictMapping> auditTypeMapping = Lists.newArrayList();
        auditTypeMapping.add(new DictMapping().build(AuditOperationType.SERVICE.getName(), AuditOperationType.SERVICE.getDesc()));
        auditTypeMapping.add(new DictMapping().build(AuditOperationType.ASSETS.getName(), AuditOperationType.ASSETS.getDesc()));
        auditTypeMapping.add(new DictMapping().build(AuditOperationType.SYSTEM.getName(), AuditOperationType.SYSTEM.getDesc()));
        mapping.put("AUDIT_TYPE", auditTypeMapping);

        List<DictMapping> logTypeMapping = Lists.newArrayList();
        logTypeMapping.add(new DictMapping().build(AuditOperationType.DELETE.getName(), AuditOperationType.DELETE.getDesc()));
        logTypeMapping.add(new DictMapping().build(AuditOperationType.UPDATE.getName(), AuditOperationType.UPDATE.getDesc()));
        logTypeMapping.add(new DictMapping().build(AuditOperationType.INSERT.getName(), AuditOperationType.INSERT.getDesc()));
        logTypeMapping.add(new DictMapping().build(AuditOperationType.LOGIN.getName(), AuditOperationType.LOGIN.getDesc()));
        logTypeMapping.add(new DictMapping().build(AuditOperationType.LOGOUT.getName(), AuditOperationType.LOGOUT.getDesc()));
        logTypeMapping.add(new DictMapping().build(AuditOperationType.ERROR.getName(), AuditOperationType.ERROR.getDesc()));
        mapping.put("LOG_TYPE", logTypeMapping);

        List<DictMapping> assetsCatalogType = Lists.newArrayList();
        assetsCatalogType.add(new DictMapping().build(AssetsCatalogType.DOMAIN.name(), "数据资产领域"));
        assetsCatalogType.add(new DictMapping().build(AssetsCatalogType.TOPIC.name(), "数据资产主题"));
        mapping.put("ASSETS_CATALOG_TYPE", assetsCatalogType);

        List<DictMapping> dataSourceType = Lists.newArrayList();
        dataSourceType.add(new DictMapping().build(DataSourceType.HIVE.name(), DataSourceType.HIVE.name().toLowerCase()));
        dataSourceType.add(new DictMapping().build(DataSourceType.MYSQL.name(), DataSourceType.MYSQL.name().toLowerCase()));
        dataSourceType.add(new DictMapping().build(DataSourceType.MSSQL.name(), DataSourceType.MSSQL.name().toLowerCase()));
        dataSourceType.add(new DictMapping().build(DataSourceType.ORACLE.name(), DataSourceType.ORACLE.name().toLowerCase()));
        dataSourceType.add(new DictMapping().build(DataSourceType.POSTGRESQL.name(), DataSourceType.POSTGRESQL.name().toLowerCase()));
        mapping.put("DATA_SOURCE_TYPE", dataSourceType);

        List<DictMapping> qualityRuleType = Lists.newArrayList();
        qualityRuleType.add(new DictMapping().build(QualityRuleType.ACCURACY.name(), "精确性"));
        qualityRuleType.add(new DictMapping().build(QualityRuleType.PROFILING.name(), "一致性"));
        qualityRuleType.add(new DictMapping().build(QualityRuleType.UNIQUENESS.name(), "唯一性"));
        qualityRuleType.add(new DictMapping().build(QualityRuleType.DISTINCT.name(), "有效性"));
        qualityRuleType.add(new DictMapping().build(QualityRuleType.TIMELINESS.name(), "及时性"));
        qualityRuleType.add(new DictMapping().build(QualityRuleType.COMPLETENESS.name(), "完整性"));
        mapping.put("QUALITY_RULE_TYPE", qualityRuleType);

        List<DictMapping> messageType = Lists.newArrayList();
        messageType.add(new DictMapping().build(MessageType.SYSTEM.name(), "系统消息"));
        mapping.put("MESSAGE_TYPE", messageType);

        return ResponseMap.success(mapping);
    }


}
