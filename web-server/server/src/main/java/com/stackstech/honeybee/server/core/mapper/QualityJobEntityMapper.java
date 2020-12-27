package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.QualityJobEntity;

public interface QualityJobEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QualityJobEntity record);

    int insertSelective(QualityJobEntity record);

    QualityJobEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QualityJobEntity record);

    int updateByPrimaryKey(QualityJobEntity record);
}