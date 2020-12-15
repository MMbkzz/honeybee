package com.stackstech.dcp.server.dataasset.service;

import com.stackstech.dcp.server.dataasset.model.ServiceModelParam;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 数据模型参数接口定义
 */
public interface ServiceModelParamService {

    /**
     * 模型参数详情查询
     *
     * @param serviceModelParam
     * @return
     */
    List<ServiceModelParam> queryAll(ServiceModelParam serviceModelParam);

    /**
     * 模型参数详情查询
     *
     * @param id
     * @return
     */
    ServiceModelParam query(Long id);

    /**
     * 新增数据模型参数
     *
     * @param serviceModelParam
     * @return
     */
    int add(ServiceModelParam serviceModelParam);

    /**
     * 模型参数删除
     *
     * @param id
     * @return
     */
    ResponseEntity<?> delete(Long id);

    /**
     * 模型参数修改
     *
     * @param serviceModelParam
     * @return
     */
    int update(ServiceModelParam serviceModelParam);
}
