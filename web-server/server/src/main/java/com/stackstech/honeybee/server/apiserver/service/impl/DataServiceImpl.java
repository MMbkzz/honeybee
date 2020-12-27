package com.stackstech.honeybee.server.apiserver.service.impl;

import com.stackstech.honeybee.server.apiserver.service.DataService;
import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.mapper.DataServiceEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DataServiceImpl
 *
 * @author william
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DataServiceEntityMapper mapper;

    @Override
    public DataServiceEntity getDataService(Long id) {
        return mapper.selectByPrimaryKey(id);
    }
}
