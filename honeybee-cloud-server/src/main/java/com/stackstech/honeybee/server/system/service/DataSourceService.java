package com.stackstech.honeybee.server.system.service;

import com.stackstech.honeybee.server.core.enums.types.DataSourceType;
import com.stackstech.honeybee.server.core.service.BaseDataService;
import com.stackstech.honeybee.server.system.entity.DBConfig;
import com.stackstech.honeybee.server.system.entity.DataSourceEntity;

public interface DataSourceService extends BaseDataService<DataSourceEntity> {

    DBConfig getDataSourceConfig(DataSourceType dataSourceType);
}
