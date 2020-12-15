package com.stackstech.dcp.server.dataservice.service;

import com.stackstech.dcp.server.dataservice.model.AppDsField;

import java.util.List;

/**
 * 数据服务APP授权字段接口定义
 */
public interface AppDsFieldService {

    /**
     * 数据服务APP授权字段查询
     *
     * @param appDsField
     * @return
     */
    List<AppDsField> queryAll(AppDsField appDsField);

    /**
     * 数据服务APP授权字段详情查询
     *
     * @param id
     * @return
     */
    AppDsField query(Long id);

    /**
     * 新增数据服务APP授权字段
     *
     * @param appDsField
     * @return
     */
    int add(AppDsField appDsField);

    /**
     * 数据服务APP授权字段删除
     *
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 数据服务APP授权字段修改
     *
     * @param appDsField
     * @return
     */
    int update(AppDsField appDsField);
}
