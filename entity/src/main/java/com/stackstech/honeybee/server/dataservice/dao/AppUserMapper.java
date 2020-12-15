package com.stackstech.honeybee.server.dataservice.dao;

import com.stackstech.honeybee.server.dataservice.model.AppUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * api服务 APP用户DAO
 */
@Mapper
@Repository
public interface AppUserMapper {

    /**
     * 新增数据APP用户
     *
     * @param appUser
     * @return
     */
    int insert(AppUser appUser);


    /**
     * 根据编号删除APP用户
     *
     * @param id
     * @return
     */
    int delete(@Param("id") String id);

    /**
     * 编辑APP用户
     *
     * @param appUser
     * @return
     */
    int update(AppUser appUser);

    /**
     * APP用户查询列表
     *
     * @param appUser
     * @return
     */
    List<AppUser> queryAll(AppUser appUser);

    int countAll(AppUser appUser);

    /**
     * 获取所有APP用户列表
     *
     * @return
     */
    List<AppUser> query(List<String> ids);

    /**
     * APP用户详情查询
     *
     * @param id
     * @return
     */
    AppUser queryByPrimaryKey(@Param("id") String id);

    /**
     * 根据名称获取App
     *
     * @param name
     * @return
     */
    AppUser queryByName(@Param("name") String name);

    /**
     * 获取自增主键
     *
     * @return
     */
    String queryPrimaryKey();

    List<AppUser> queryByCondition(@Param("queryString") String queryString);

    int countByCondition(@Param("queryString") String queryString);

    AppUser queryByPrimaryKeyAndOwner(@Param("id") String appId, @Param("userId") String userId);
}
