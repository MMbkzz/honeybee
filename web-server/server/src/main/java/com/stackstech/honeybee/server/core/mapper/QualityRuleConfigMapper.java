package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.QualityRuleConfig;

public interface QualityRuleConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QualityRuleConfig record);

    int insertSelective(QualityRuleConfig record);

    QualityRuleConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QualityRuleConfig record);

    int updateByPrimaryKey(QualityRuleConfig record);
}