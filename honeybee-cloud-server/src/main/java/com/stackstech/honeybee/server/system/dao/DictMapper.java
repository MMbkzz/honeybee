package com.stackstech.honeybee.server.system.dao;

import com.stackstech.honeybee.server.system.entity.DictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DictMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictEntity record);

    int insertSelective(DictEntity record);

    DictEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictEntity record);

    int updateByPrimaryKey(DictEntity record);

    List<DictEntity> selectByCatalogName(String catalogName);
}