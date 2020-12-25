package com.stackstech.honeybee.server.dataservice.dao;

import com.stackstech.honeybee.server.dataservice.model.AppDs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * api服务 APP授权DAO
 */
@Mapper
@Repository
public interface AppDsMapper {

    /**
     * 新增数据APP授权
     *
     * @param appDs
     * @return
     */
    int insert(AppDs appDs);


    /**
     * 根据编号删除APP授权
     *
     * @param id
     * @return
     */
    int delete(@Param("id") Long id);

    /**
     * 编辑APP授权
     *
     * @param appDs
     * @return
     */
    int update(AppDs appDs);

    /**
     * APP授权查询列表
     *
     * @param appDs
     * @return
     */
    List<AppDs> queryAll(AppDs appDs);

    /**
     * APP授权详情查询
     *
     * @param id
     * @return
     */
    AppDs queryByPrimaryKey(@Param("id") Long id);

    /**
     * 根据服务id获取AppDs实体
     *
     * @param dataServiceId
     * @return
     */
    List<AppDs> queryByServiceId(@Param("dataServiceId") String dataServiceId);

    /**
     * 根据服务用户id获取AppDs实体
     *
     * @param appId
     * @return
     */
    List<AppDs> queryByAppId(@Param("appId") String appId);

    /**
     * 根据AppId和DataserviceId获取AppDs
     *
     * @param dataServiceId
     * @param appId
     * @return
     */
    AppDs queryByServiceIdAndAppId(@Param("dataServiceId") String dataServiceId, @Param("appId") String appId);

    /**
     * 根据AppId和DataserviceId删除AppDs
     *
     * @param dataServiceId
     * @param appId
     * @return
     */
    int deleteByServiceIdAndAppId(@Param("dataServiceId") String dataServiceId, @Param("appId") String appId);

    /**
     * 根据数据服务id删除AppDs
     *
     * @param dataServiceId
     * @return
     */
    int deleteByServiceId(@Param("dataServiceId") String dataServiceId);

    /**
     * 根据AppId删除AppDs
     *
     * @param appId
     * @return
     */
    int deleteByAppId(@Param("appId") String appId);

    /**
     * token + appId + dataServiceId
     *
     * @param appDs
     * @return
     */
    AppDs queryByToken(AppDs appDs);
}
