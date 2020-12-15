package com.stackstech.dcp.server.dataasset.dao;

import com.stackstech.dcp.server.dataasset.model.ServiceModelField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 服务模型字段DAO
 */
@Mapper
@Repository
public interface ServiceModelFieldMapper {

    /**
     * 新增数据模型字段
     *
     * @param serviceModelField
     * @return
     */
    int insert(ServiceModelField serviceModelField);


    /**
     * 根据编号删除模型字段
     *
     * @param id
     * @return
     */
    int delete(@Param("id") Long id);

    /**
     * 编辑模型字段
     *
     * @param serviceModelField
     * @return
     */
    int update(ServiceModelField serviceModelField);

    /**
     * 模型字段查询列表
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
    ServiceModelField queryByPrimaryKey(@Param("id") Long id);

    List<ServiceModelField> queryByServiceId(@Param("serviceModelId") String serviceModelId);

    int deleteByServiceId(@Param("serviceModelId") String serviceModelId);
}
