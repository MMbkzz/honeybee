package com.stackstech.honeybee.server.auth.dao;

import com.stackstech.honeybee.server.auth.model.AuthRole;
import com.stackstech.honeybee.server.auth.model.AuthUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关系表Dao
 * <p>
 * 表名 dcp_auth_user_role
 */
@Mapper
@Repository
public interface AuthUserRoleMapper {

    /**
     * 添加用户角色关系
     *
     * @param userRole
     * @return
     */
    int insert(AuthUserRole userRole);

    /**
     * 根据用户编号删除用户角色关系
     *
     * @param userId
     * @return
     */
    int deleteByUserId(Long userId);

    /**
     * 根据角色编号查询用户角色
     *
     * @param roleId
     * @return
     */
    List<AuthUserRole> selectByRoleId(Long roleId);

    /**
     * 根据用户编号查询用户角色
     *
     * @param userId
     * @return
     */
    List<AuthUserRole> selectByUserId(Long userId);

    /**
     * 查询用户角色
     *
     * @param userId
     * @return
     */
    List<AuthRole> getRoleByUser(Long userId);

}
