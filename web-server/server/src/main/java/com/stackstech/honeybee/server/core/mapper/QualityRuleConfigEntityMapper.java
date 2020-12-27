package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.QualityRuleConfigEntity;

public interface QualityRuleConfigEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QualityRuleConfigEntity record);

    int insertSelective(QualityRuleConfigEntity record);

    QualityRuleConfigEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QualityRuleConfigEntity record);

    int updateByPrimaryKey(QualityRuleConfigEntity record);
}