package com.stackstech.honeybee.server.dataasset.dao;

import com.stackstech.honeybee.server.dataasset.model.ServiceModelParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据模型参数DAO
 */
@Mapper
@Repository
public interface ServiceModelParamMapper {

    /**
     * 新增数据模型参数
     *
     * @param serviceModelParam
     * @return
     */
    int insert(ServiceModelParam serviceModelParam);


    /**
     * 根据编号删除模型参数
     *
     * @param id
     * @return
     */
    int delete(@Param("id") Long id);

    /**
     * 编辑模型参数
     *
     * @param serviceModelParam
     * @return
     */
    int update(ServiceModelParam serviceModelParam);

    /**
     * 模型参数查询列表
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
    ServiceModelParam queryByPrimaryKey(@Param("id") Long id);

    List<ServiceModelParam> queryByServiceId(@Param("serviceModelId") String serviceModelId);

    int deleteByServiceId(@Param("serviceModelId") String serviceModelId);
}
