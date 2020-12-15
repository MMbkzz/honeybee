package com.stackstech.dcp.server.dataservice.service;

import com.stackstech.dcp.server.dataservice.model.AppDs;

import java.util.List;

/**
 * 数据服务App授权接口定义
 */
public interface AppDsService {

    /**
     * 数据服务App授权查询
     *
     * @param appDs
     * @return
     */
    List<AppDs> queryAll(AppDs appDs);

    /**
     * 数据服务App授权详情查询
     *
     * @param id
     * @return
     */
    AppDs query(Long id);

    /**
     * 新增数据服务App授权
     *
     * @param appDs
     * @return
     */
    int add(AppDs appDs);

    /**
     * 数据服务App授权删除
     *
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 数据服务App授权修改
     *
     * @param appDs
     * @return
     */
    int update(AppDs appDs);
}
