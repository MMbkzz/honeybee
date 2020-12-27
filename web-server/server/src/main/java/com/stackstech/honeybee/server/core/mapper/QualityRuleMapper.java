package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.QualityRuleEntity;

public interface QualityRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QualityRuleEntity record);

    int insertSelective(QualityRuleEntity record);

    QualityRuleEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QualityRuleEntity record);

    int updateByPrimaryKey(QualityRuleEntity record);
}