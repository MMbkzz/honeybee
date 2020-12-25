package com.stackstech.honeybee.server.auth.dao;

import com.stackstech.honeybee.server.auth.model.AuthRole;
import com.stackstech.honeybee.server.auth.model.vo.RolePermissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色表dao
 * 表名 dgp_auth_role
 * 映射 AuthRoleMapper.xml
 */
@Mapper
@Repository
public interface AuthRoleMapper {
    /**
     * 查询是否有该角色
     *
     * @param name
     * @return
     */
    List<AuthRole> selectByName(String name);

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    int insert(AuthRole role);

    /**
     * 根据角色编号删除角色
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 编辑角色
     *
     * @param role
     * @return
     */
    int updateByRoleId(AuthRole role);

    /**
     * 根据用户编号查询所属角色
     *
     * @param userId 用户编号
     * @return
     */
    List<AuthRole> getRoles(Long userId);

    /**
     * 角色启用停用
     *
     * @param role
     * @return
     */
    int startStopRole(AuthRole role);

    /**
     * 角色查询列表
     *
     * @param vo
     * @return
     */
    List<RolePermissionVo> getPageRoles(RolePermissionVo vo);

    /**
     * 角色详情
     *
     * @param roleId
     * @return
     */
    AuthRole getRoleInfo(Long roleId);

    /**
     * 根据用户名查询角色
     *
     * @param userId
     * @return
     */
    List<AuthRole> getRoleByUser(Long userId);

    /**
     * 根据状态查询所有角色信息
     *
     * @return
     */
    List<AuthRole> selectAllRole(String status);

    /**
     * 校验角色是否存在
     *
     * @param role
     * @return
     */
    List<AuthRole> verifyRoleExists(AuthRole role);

}
