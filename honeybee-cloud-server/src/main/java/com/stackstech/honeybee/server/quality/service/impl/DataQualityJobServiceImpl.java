package com.stackstech.honeybee.server.quality.service.impl;

import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.core.handler.MessageHandler;
import com.stackstech.honeybee.server.core.service.BaseDataService;
import com.stackstech.honeybee.server.quality.dao.QualityJobMapper;
import com.stackstech.honeybee.server.quality.entity.QualityJobEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(rollbackFor = Exception.class)
@Service
public class DataQualityJobServiceImpl implements BaseDataService<QualityJobEntity> {

    @Autowired
    private QualityJobMapper mapper;

    @Override
    public boolean add(QualityJobEntity entity) throws ServerException {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(QualityJobEntity entity) throws ServerException {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) throws ServerException {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public QualityJobEntity getSingle(Long recordId) throws ServerException, DataNotFoundException {
        QualityJobEntity entity = mapper.selectByPrimaryKey(recordId);
        CommonUtil.isNull(entity, MessageHandler.of().message("data.not.found"));
        return entity;
    }

    @Override
    public List<QualityJobEntity> get(Map<String, Object> parameter) throws ServerException, DataNotFoundException {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) throws ServerException {
        return mapper.selectTotalCount(parameter);
    }

}
