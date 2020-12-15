package com.stackstech.honeybee.server.dataservice.dao;

import com.stackstech.honeybee.server.dataservice.model.AppDsField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * api服务 APP授权字段DAO
 */
@Mapper
@Repository
public interface AppDsFieldMapper {

    /**
     * 新增数据APP授权字段
     *
     * @param appDsField
     * @return
     */
    int insert(AppDsField appDsField);


    /**
     * 根据编号删除APP授权字段
     *
     * @param id
     * @return
     */
    int delete(@Param("id") Long id);

    /**
     * 编辑APP授权字段
     *
     * @param appDsField
     * @return
     */
    int update(AppDsField appDsField);

    /**
     * APP授权字段查询列表
     *
     * @param appDsField
     * @return
     */
    List<AppDsField> queryAll(AppDsField appDsField);

    /**
     * APP授权字段详情查询
     *
     * @param id
     * @return
     */
    AppDsField queryByPrimaryKey(@Param("id") Long id);

    /**
     * 根据AppId和ServiceId获取授权字段列表
     *
     * @param appId
     * @param dataServiceId
     * @return
     */
    List<AppDsField> queryByServiceIdAndAppId(@Param("dataServiceId") String dataServiceId, @Param("appId") String appId);

    /**
     * 根据AppId和DataServiceId删除AppDsField
     *
     * @param dataServiceId
     * @param appId
     * @return
     */
    int deleteByServiceIdAndAppId(@Param("dataServiceId") String dataServiceId, @Param("appId") String appId);

    /**
     * 根据服务Id获取AppdsField
     *
     * @param dataServiceId
     * @return
     */
    List<AppDsField> queryByServiceId(@Param("dataServiceId") String dataServiceId);

    /**
     * 根据服务Id获取字段
     *
     * @param dataServiceId
     * @return
     */
    int deleteByServiceId(@Param("dataServiceId") String dataServiceId);

    /**
     * 根据用户Id获取字段
     *
     * @param id
     * @return
     */
    List<AppDsField> queryByAppId(String id);

    /**
     * 根据用户Id删除字段
     *
     * @param id
     * @return
     */
    int deleteByAppId(String id);
}
