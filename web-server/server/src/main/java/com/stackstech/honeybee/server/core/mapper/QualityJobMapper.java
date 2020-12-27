package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.QualityJobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QualityJobMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QualityJobEntity record);

    int insertSelective(QualityJobEntity record);

    QualityJobEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QualityJobEntity record);

    int updateByPrimaryKey(QualityJobEntity record);
}