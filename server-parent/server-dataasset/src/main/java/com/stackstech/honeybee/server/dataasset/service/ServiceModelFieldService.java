package com.stackstech.honeybee.server.dataasset.service;

import com.stackstech.honeybee.server.dataasset.model.ServiceModelField;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 数据模型字段接口定义
 */
public interface ServiceModelFieldService {

    /**
     * 模型字段详情查询
     *
     * @param serviceModelField
     * @return
     */
    List<ServiceModelField> queryAll(ServiceModelField serviceModelField);

    /**
     * 模型字段详情查询
     *
     * @param id
     * @return
     */
    ServiceModelField query(Long id);

    /**
     * 新增数据模型字段
     *
     * @param serviceModelField
     * @return
     */
    int add(ServiceModelField serviceModelField);

    /**
     * 模型字段删除
     *
     * @param id
     * @return
     */
    ResponseEntity<?> delete(Long id);

    /**
     * 模型字段修改
     *
     * @param serviceModelField
     * @return
     */
    int update(ServiceModelField serviceModelField);
}
