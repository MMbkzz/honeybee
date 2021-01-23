package com.stackstech.honeybee.server.system.service.impl;

import com.stackstech.honeybee.server.system.entity.DataSourceEntity;
import com.stackstech.honeybee.server.core.vo.DataSourceVo;
import com.stackstech.honeybee.server.system.dao.DataSourceMapper;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataSourceServiceImpl implements DataService<DataSourceVo, DataSourceEntity> {

    @Autowired
    private DataSourceMapper mapper;

    @Override
    public boolean add(DataSourceVo vo, Long ownerId) {
        DataSourceEntity entity = new DataSourceEntity().build(ownerId);
        BeanUtils.copyProperties(vo, entity);
        //TODO
        entity.setDatasourceConfig(CommonUtil.toJsonString(vo.getDatasourceParameters()));
        entity.setDatasourceCode(CommonUtil.generateEntityCode());
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataSourceVo vo, Long ownerId) {
        DataSourceEntity entity = new DataSourceEntity().update(ownerId);
        entity.setDatasourceConfig(CommonUtil.toJsonString(vo.getDatasourceParameters()));
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

}
