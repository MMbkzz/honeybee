package com.stackstech.honeybee.server.system.service;

import com.stackstech.honeybee.server.core.enums.types.DataSourceType;
import com.stackstech.honeybee.server.system.entity.DBConfig;
import com.stackstech.honeybee.server.system.entity.DataSourceEntity;

import java.util.List;
import java.util.Map;

public interface DataSourceService {

    boolean add(DataSourceEntity entity);

    boolean update(DataSourceEntity entity);

    boolean delete(Long recordId, Long ownerId);

    DataSourceEntity getSingle(Long recordId);

    List<DataSourceEntity> get(Map<String, Object> parameter);

    Integer getTotalCount(Map<String, Object> parameter);

    DBConfig getDataSourceConfig(DataSourceType dataSourceType);
}
