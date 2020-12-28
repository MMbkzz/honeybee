package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.QualityRuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QualityRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(QualityRuleEntity record);

    QualityRuleEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QualityRuleEntity record);

}