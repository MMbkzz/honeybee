package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.core.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.core.vo.AssetsModelVo;
import com.stackstech.honeybee.server.dao.AssetsModelMapper;
import com.stackstech.honeybee.server.service.DataService;
import com.stackstech.honeybee.server.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AssetsModelServiceImpl implements DataService<AssetsModelVo> {

    @Autowired
    private AssetsModelMapper mapper;

    @Override
    public boolean add(AssetsModelVo vo, Long ownerId) {
        AssetsModelEntity entity = new AssetsModelEntity().create(ownerId);
        BeanUtils.copyProperties(vo, entity);
        entity.setAssetsModelCode(CommonUtil.generateEntityCode());
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AssetsModelVo vo, Long ownerId) {
        AssetsModelEntity entity = new AssetsModelEntity().update(ownerId);
        BeanUtils.copyProperties(vo, entity);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        //TODO insert to data recycler
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public Object getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public Object get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }
}
