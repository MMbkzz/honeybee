package com.stackstech.honeybee.server.param.service;


import com.stackstech.honeybee.server.param.model.Parameter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 参数Service接口
 */
public interface ParameterService {

    List<Parameter> queryAll(Parameter parameter) throws Exception;

    Parameter query(@Param("id") Long id) throws Exception;

    Parameter queryByParamName(Parameter parameter) throws Exception;

    int insert(Parameter parameter) throws Exception;

    int update(Parameter parameter) throws Exception;

    int delete(Long id) throws Exception;

}
