package com.stackstech.honeybee.server.quality.dao;

import com.stackstech.honeybee.server.quality.entity.QualityRuleConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QualityRuleConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(QualityRuleConfig record);

    QualityRuleConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QualityRuleConfig record);

}