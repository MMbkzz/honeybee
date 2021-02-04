package com.stackstech.honeybee.server.system.controller;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.stackstech.honeybee.common.entity.ResponseMap;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.annotation.ApiAuthIgnore;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.DictCatalog;
import com.stackstech.honeybee.server.core.enums.SysConfigMap;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.system.entity.*;
import com.stackstech.honeybee.server.system.service.DataCacheService;
import com.stackstech.honeybee.server.system.service.DictService;
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
    @Autowired
    private DictService dictService;

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
        List<DataCacheEntity> data = dataCacheService.get(parameters.getKeywords(), parameters.getPageStart(), parameters.getPageSize());
        if (data != null && data.size() > 0) {
            int total = dataCacheService.getTotalCount(parameters.getKeywords());
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
    public ResponseMap<?> getDict() {
        List<DictEntity> dicts = dictService.getDictByCatalog(null);
        Map<String, List<DictMapping>> maps = Maps.newHashMap();
        List<DictMapping> mapping = null;
        for (DictEntity entity : dicts) {
            if (maps.get(entity.getCatalogName()) == null) {
                mapping = Lists.newArrayList();
            } else {
                mapping = maps.get(entity.getCatalogName());
            }
            mapping.add(new DictMapping().build(entity.getCode(), entity.getName()));
            maps.put(entity.getCatalogName(), mapping);
        }
        return ResponseMap.success(maps);
    }

    @ApiAuthIgnore
    @ApiOperation(value = "get global dict mapping by catalog")
    @RequestMapping(value = "/system/dict/{catalog}", method = RequestMethod.GET)
    public ResponseMap<?> getDict(@NotNull(message = "catalog cannot be null") @PathVariable("catalog") String catalog) {
        DictCatalog dictCatalog = null;
        try {
            dictCatalog = DictCatalog.valueOf(catalog);
            if (dictCatalog == null) {
                throw new IllegalArgumentException("invalid catalog name [" + catalog + "]");
            }
        } catch (IllegalArgumentException e) {
            return ResponseMap.failed("invalid catalog name");
        }

        List<DictEntity> dicts = dictService.getDictByCatalog(dictCatalog);
        List<DictMapping> mapping = Lists.newArrayList();
        for (DictEntity entity : dicts) {
            mapping.add(new DictMapping().build(entity.getCode(), entity.getName()));
        }
        Map<String, List<DictMapping>> maps = Maps.newHashMap();
        maps.put(catalog, mapping);

        return ResponseMap.success(mapping);
    }


}
