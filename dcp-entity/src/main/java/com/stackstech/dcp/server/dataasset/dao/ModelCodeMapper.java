package com.stackstech.dcp.server.dataasset.dao;

import com.stackstech.dcp.server.dataasset.model.ModelCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Model快码Mapper
 */
@Mapper
@Repository
public interface ModelCodeMapper {

    List<ModelCode> queryAll(Map<String, Object> map);

    ModelCode queryByPrimaryKey(@Param("id") Long id);

    ModelCode queryByCode(@Param("code") String code);

    List<ModelCode> queryByType(@Param("type") String type);

    ModelCode queryCode(Map<String, Object> map);

    List<ModelCode> queryByParentCode(@Param("parentCode") String parentCode);
}
