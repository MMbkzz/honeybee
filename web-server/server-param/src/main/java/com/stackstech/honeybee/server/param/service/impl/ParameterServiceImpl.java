package com.stackstech.honeybee.server.param.service.impl;

import com.stackstech.honeybee.server.param.dao.ParameterMapper;
import com.stackstech.honeybee.server.param.model.Parameter;
import com.stackstech.honeybee.server.param.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 参数Service实现类
 */
@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    private ParameterMapper parameterMapper;

    /**
     * 获取参数列表
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    @Override
    public List<Parameter> queryAll(Parameter parameter) throws Exception {
        return parameterMapper.queryAll(parameter);
    }

    /**
     * 获取参数
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Parameter query(Long id) throws Exception {
        return parameterMapper.queryByPrimaryKey(id);
    }

    /**
     * 根据名称获取参数
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    @Override
    public Parameter queryByParamName(Parameter parameter) throws Exception {
        return parameterMapper.queryByParamName(parameter);
    }

    /**
     * 新增参数
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    @Override
    public int insert(Parameter parameter) throws Exception {
        return parameterMapper.insert(parameter);
    }

    /**
     * 更新参数
     *
     * @param parameter
     * @return
     * @throws Exception
     */
    @Override
    public int update(Parameter parameter) throws Exception {
        return parameterMapper.update(parameter);
    }

    /**
     * 删除参数
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public int delete(Long id) throws Exception {
        return parameterMapper.delete(id);
    }
}
