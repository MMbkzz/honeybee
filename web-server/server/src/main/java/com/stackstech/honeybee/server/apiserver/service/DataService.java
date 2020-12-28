package com.stackstech.honeybee.server.apiserver.service;

import com.stackstech.honeybee.server.core.entity.DataServiceEntity;

import java.util.List;
import java.util.Map;

/**
 * DataService
 *
 * @author william
 */
public interface DataService {

    DataServiceEntity getDataService(Long id);

    List<DataServiceEntity> getDataServices(Map<String, Object> parameter);

    Integer getDataServiceCount(Map<String, Object> parameter);

    Boolean deleteDataService(Long id);

    Boolean updateDataService(DataServiceEntity entity);

}
