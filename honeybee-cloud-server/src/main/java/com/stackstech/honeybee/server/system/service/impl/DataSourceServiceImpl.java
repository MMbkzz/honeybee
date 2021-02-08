package com.stackstech.honeybee.server.system.service.impl;

import com.stackstech.honeybee.common.entity.JsonParameterMap;
import com.stackstech.honeybee.server.core.conf.ApplicationConfig;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.types.DataSourceType;
import com.stackstech.honeybee.server.system.dao.DataSourceMapper;
import com.stackstech.honeybee.server.system.entity.DBConfig;
import com.stackstech.honeybee.server.system.entity.DataSourceEntity;
import com.stackstech.honeybee.server.system.service.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper mapper;
    @Autowired
    private ApplicationConfig applicationConfig;

    private void setDatasourceConfig(DataSourceEntity entity) {
        Map<String, Object> parameters = entity.getDataSourceVo().getDatasourceParameters();
        DBConfig dbConfig = getDataSourceConfig(entity.getDatasourceType());
        // use custom parameter or default
        if (parameters != null && parameters.size() > 0) {
            dbConfig.setConfig(parameters);
        }
        JsonParameterMap conf = new JsonParameterMap();
        conf.put("config", dbConfig);
        entity.setDatasourceConfig(conf);
    }

    @Override
    public boolean add(DataSourceEntity entity) {
        setDatasourceConfig(entity);
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataSourceEntity entity) {
        setDatasourceConfig(entity);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public DataSourceEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<DataSourceEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }

    @Override
    public DBConfig getDataSourceConfig(DataSourceType dataSourceType) {
        Assert.notNull(applicationConfig.getConfigPath(), "config path cannot be null");

        String fileName = StringUtils.join("config-", dataSourceType.getName().toLowerCase(), ".yml");
        String dbConfig = StringUtils.join(
                applicationConfig.getConfigPath(), File.separatorChar,
                Constant.DB_CONF_DIR, File.separatorChar,
                fileName);
        DBConfig config = null;
        try (FileInputStream inputStream = new FileInputStream(dbConfig)) {
            Yaml yaml = new Yaml();
            Map<String, Object> maps = yaml.load(inputStream);
            if (maps != null) {
                config = new DBConfig();
                config.setDataSourceType(DataSourceType.valueOf(maps.get("type").toString().toUpperCase()));
                config.setVersion(maps.get("version").toString());
                config.setConnector(maps.get("connector").toString());
                config.setConfig(maps.get("config"));
            }
        } catch (Exception e) {
            log.error("load db config yaml error", e);
        }
        return config;
    }

}
