package com.stackstech.dcp.server.auth.dao;

import com.stackstech.dcp.server.auth.model.AuthOperation;
import com.stackstech.dcp.server.auth.model.AuthPermission;
import com.stackstech.dcp.server.auth.model.AuthResource;
import com.stackstech.dcp.server.auth.model.AuthRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限
 * <p>
 * <p>
 * 表名 AUTH_PERMISSION
 */
@Mapper
@Repository
public interface AuthPermissionMapper {
    /**
     * 获取所有资源
     *
     * @return
     */
    List<AuthResource> getResources();

    /**
     * 获取该资源的操作
     *
     * @param resource
     * @return
     */
    List<AuthOperation> getOperations(AuthResource resource);

    /**
     * 批量插入权限
     *
     * @param list
     */
    void insertPermissionBatch(List<AuthPermission> list);

    /**
     * 获取角色的权限
     *
     * @param role
     * @return
     */
    List<AuthPermission> getPermissions(AuthRole role);

    /**
     * 根据角色编号删除角色权限关系
     *
     * @param role
     */
    void deletePermissions(AuthRole role);

    /**
     * 新增角色权限关系
     *
     * @param record
     * @return
     */
    int insert(AuthPermission record);

    /**
     * 根据资源 ID 查询相对应的权限
     *
     * @param permissions
     * @return
     */
    List<AuthPermission> selectByResource(List<AuthPermission> permissions);

    /**
     * 通过资源 id 得到相应权限
     *
     * @param resourceId
     * @return
     */
    List<AuthPermission> getPermissionsByResourceId(Long resourceId);


    /**
     * 删除权限
     */
    int deletePermission(String id);

    /**
     * 批量删除权限
     */
    int deletePermissionList(@Param("ids") List<String> ids);

}
