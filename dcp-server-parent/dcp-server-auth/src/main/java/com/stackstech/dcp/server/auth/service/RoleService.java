package com.stackstech.dcp.server.auth.service;

import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.server.auth.model.AuthRole;
import com.stackstech.dcp.server.auth.model.vo.ResourceOperations;
import com.stackstech.dcp.server.auth.model.vo.RolePermissionVo;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface RoleService {
    /**
     * 新增角色
     *
     * @param role
     * @param userId
     * @return
     */
    Boolean addRole(AuthRole role, long userId);

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    String delRole(Long id);

    /**
     * 编辑角色
     *
     * @param role
     * @param userId
     * @return
     */
    int editRole(AuthRole role, long userId);

    /**
     * 角色启用停用
     *
     * @param id,status
     * @return
     */
    Boolean startStopRole(Long id, String status, long userId);

    /**
     * 通过状态查询所有角色信息
     *
     * @return
     */
    List<AuthRole> selectAllRole(String status);

    /**
     * 查询角色列表
     *
     * @param vo,page
     * @return
     */
    Map<String, Object> getPageRoles(RolePermissionVo vo, Page page);

    /**
     * 角色详情
     *
     * @param id
     * @return
     */
    Map<String, Object> getRoleInfo(Long id);

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    List<AuthRole> getRoleByUser(Long userId);

    /**
     * 校验角色是否存在
     *
     * @param role
     * @return
     */
    boolean verifyRoleExists(AuthRole role);

    /**
     * 构建资源操作 tree
     *
     * @return
     */
    List<ResourceOperations> buildResourceOperations();

    /**
     * 新增权限
     *
     * @param vo
     * @return
     */
    ResponseEntity<?> addRolePermission(RolePermissionVo vo, long userId);

    /**
     * 编辑权限
     *
     * @param vo
     * @return
     */
    ResponseEntity<?> editRolePermission(RolePermissionVo vo, long userId);

}
