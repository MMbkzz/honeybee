package com.stackstech.honeybee.server.api.service;

import com.stackstech.honeybee.server.api.entity.DataServiceElement;
import com.stackstech.honeybee.server.api.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.service.BaseDataService;

import java.util.List;

public interface DataService extends BaseDataService<DataServiceEntity> {

    List<DataServiceElement> getDataServiceList();
}
