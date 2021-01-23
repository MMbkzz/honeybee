package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.quality.entity.QualityJobEntity;
import com.stackstech.honeybee.server.core.vo.QualityJobVo;
import com.stackstech.honeybee.server.quality.dao.QualityJobMapper;
import com.stackstech.honeybee.server.service.DataService;
import com.stackstech.honeybee.server.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataQualityJobServiceImpl implements DataService<QualityJobVo, QualityJobEntity> {

    @Autowired
    private QualityJobMapper mapper;

    @Override
    public boolean add(QualityJobVo vo, Long ownerId) {
        QualityJobEntity entity = new QualityJobEntity().build(ownerId);
        BeanUtils.copyProperties(vo, entity);
        entity.setJobCode(CommonUtil.generateEntityCode());
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(QualityJobVo vo, Long ownerId) {
        QualityJobEntity entity = new QualityJobEntity().update(ownerId);
        BeanUtils.copyProperties(vo, entity);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public QualityJobEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<QualityJobEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }

}
