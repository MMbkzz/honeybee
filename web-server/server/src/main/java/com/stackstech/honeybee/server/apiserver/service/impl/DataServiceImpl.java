package com.stackstech.honeybee.server.apiserver.service.impl;

import com.stackstech.honeybee.server.apiserver.service.DataService;
import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.mapper.DataServiceMapper;
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
    private DataServiceMapper dataServiceMapper;

    @Override
    public DataServiceEntity getDataService(Long id) {
        return dataServiceMapper.selectByPrimaryKey(id);
    }
}
