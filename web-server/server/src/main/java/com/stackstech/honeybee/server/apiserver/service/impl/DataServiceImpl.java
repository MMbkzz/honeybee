package com.stackstech.honeybee.server.apiserver.service.impl;

import com.stackstech.honeybee.server.apiserver.service.DataService;
import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.mapper.DataServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * DataServiceImpl
 *
 * @author william
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DataServiceMapper mapper;

    @Override
    public DataServiceEntity getDataService(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DataServiceEntity> getDataServices(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getDataServiceCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }

    @Override
    public Boolean deleteDataService(Long id) {
        return mapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Boolean updateDataService(DataServiceEntity entity) {
        if (entity.getId() != null) {
            return mapper.updateByPrimaryKeySelective(entity) > 0;
        }
        return mapper.insertSelective(entity) > 0;
    }
}
