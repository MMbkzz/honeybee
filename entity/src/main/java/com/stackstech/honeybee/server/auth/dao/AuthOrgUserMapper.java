package com.stackstech.honeybee.server.auth.dao;

import com.stackstech.honeybee.server.auth.model.AuthOrgUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 组织结构用户关系表
 * <p>
 * <p>
 * 表名 AUTH_ORG_USER
 */
@Mapper
@Repository
public interface AuthOrgUserMapper {

    /**
     * 新增用户组织关系
     *
     * @param record
     * @return
     */
    int insert(AuthOrgUser record);

    /**
     * 根据用户编号删除组织结构关系
     *
     * @param userId
     * @return
     */
    int deleteByUserId(Long userId);

    /**
     * 根据组织编号查询用户
     *
     * @param orgId
     * @return
     */
    List<AuthOrgUser> selectByOrgId(Long orgId);

}
