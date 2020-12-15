package com.stackstech.dcp.server.param.dao;

import com.stackstech.dcp.server.param.model.Parameter;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * parameter mapper interface
 */
@Repository
@Mapper
public interface ParameterMapper {

    List<Parameter> queryAll(Parameter parameter);

    Parameter queryByPrimaryKey(@Param("id") Long id);

    Parameter queryByParamName(Parameter parameter);

    int insert(Parameter parameter);

    int update(Parameter parameter);

    int delete(@Param("id") Long id);

    @MapKey("param_name")
    Map<String, Object> queryParams(@Param("relObjectType") String relObjectType, @Param("relObjectId") String relObjectId);

    int deleteByObjectId(@Param("objectId") String objectId);
}
