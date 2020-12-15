package com.stackstech.honeybee.server.auth.service.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.core.page.IProcessPage;
import com.stackstech.honeybee.core.page.Page;
import com.stackstech.honeybee.core.page.PageUtil;
import com.stackstech.honeybee.core.util.SnowFlake;
import com.stackstech.honeybee.server.auth.dao.AuthPermissionMapper;
import com.stackstech.honeybee.server.auth.dao.AuthRoleMapper;
import com.stackstech.honeybee.server.auth.dao.AuthUserRoleMapper;
import com.stackstech.honeybee.server.auth.model.*;
import com.stackstech.honeybee.server.auth.model.vo.PermissionVo;
import com.stackstech.honeybee.server.auth.model.vo.ResourceOperations;
import com.stackstech.honeybee.server.auth.model.vo.RolePermissionVo;
import com.stackstech.honeybee.server.auth.service.RoleService;
import com.stackstech.honeybee.server.auth.tree.BuildPermissionTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private AuthRoleMapper authRoleMapper;
    @Autowired
    private AuthUserRoleMapper authUserRoleMapper;
    @Autowired
    private AuthPermissionMapper authPermissionMapper;

    @Autowired
    private SnowFlake snowflake;
    @Autowired
    private IProcessPage iProcessPage;

    /**
     * 新增角色
     *
     * @param role
     * @param userId
     * @return
     */
    @Override
    public Boolean addRole(AuthRole role, long userId) {
        List<AuthRole> roles = authRoleMapper.selectByName(role.getName());
        if (roles.size() > 0) {
            return false;
        } else {
            role.setId(snowflake.next());
            role.setCreateTime(new Date());
            role.setCreateBy(userId);
            role.setUpdateTime(new Date());
            //新增角色
            role.setStatus("1");
            authRoleMapper.insert(role);
            return true;
        }
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @Override
    public String delRole(Long id) {
        // 查询这个角色下绑定用户
        List<AuthUserRole> userRoles = authUserRoleMapper.selectByRoleId(id);
        if (userRoles.size() > 0) {
            return "角色已经有用户使用";
        }
        // 删除角色信息
        authRoleMapper.deleteByPrimaryKey(id);
        // 删除权限信息
        AuthRole role = new AuthRole();
        role.setId(id);
        authPermissionMapper.deletePermissions(role);
        return "success";
    }

    /**
     * 编辑角色
     *
     * @param role
     * @param userId
     * @return
     */
    @Override
    public int editRole(AuthRole role, long userId) {
        //修改角色信息
        role.setUpdateTime(new Date());
        role.setUpdateBy(userId);
        authRoleMapper.updateByRoleId(role);
        return 1;
    }

    /**
     * 角色启用停用
     *
     * @param id,status
     * @return
     */
    @Override
    public Boolean startStopRole(Long id, String status, long userId) {
        AuthRole role = new AuthRole();
        role.setId(id);
        role.setStatus(status);
        role.setUpdateTime(new Date());
        role.setUpdateBy(userId);
        authRoleMapper.startStopRole(role);
        return true;
    }

    /**
     * 通过状态查询所有角色信息
     *
     * @return
     */
    @Override
    public List<AuthRole> selectAllRole(String status) {
        return authRoleMapper.selectAllRole(status);
    }

    /**
     * 查询角色列表
     *
     * @param vo,page
     * @return
     */
    @Override
    public Map<String, Object> getPageRoles(RolePermissionVo vo, Page page) {
        if (null == page.getPageNo() && null == page.getPageSize()) {
            page.setPageNo(1);
            page.setPageSize(10);
        }
        PageUtil.page(page);
        List<RolePermissionVo> list = authRoleMapper.getPageRoles(vo);
        for (RolePermissionVo rolePermissionVo : list) {
            AuthRole role = new AuthRole();
            role.setId(rolePermissionVo.getId());
            List<AuthPermission> permissions = authPermissionMapper.getPermissions(role);
            Map<Long, List<String>> map = new HashMap<Long, List<String>>(0);
            for (AuthPermission per : permissions) {
                List<String> strings = map.get(per.getResourceId());
                if (null == strings) {
                    strings = new ArrayList<String>(0);
                }
                strings.add(per.getOperationId().toString());
                map.put(per.getResourceId(), strings);
            }
            List<PermissionVo> permissionVos = new ArrayList<PermissionVo>(0);
            for (Map.Entry<Long, List<String>> entry : map.entrySet()) {
                PermissionVo permissionVo = new PermissionVo();
                permissionVo.setResourceId(entry.getKey());
                permissionVo.setOps(Joiner.on(",").join(entry.getValue()));
                permissionVos.add(permissionVo);
            }
            rolePermissionVo.setPermissionVos(permissionVos);
        }
        return iProcessPage.process(list);
    }

    /**
     * 角色详情
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getRoleInfo(Long id) {
        //获取角色信息
        AuthRole role = authRoleMapper.getRoleInfo(id);
        Map<String, Object> result = Maps.newHashMap();
        result.put("role", role);
        return result;
    }

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<AuthRole> getRoleByUser(Long userId) {
        List<AuthRole> roles = authUserRoleMapper.getRoleByUser(userId);
        return roles;
    }

    /**
     * 校验角色是否存在
     *
     * @param role
     * @return
     */
    @Override
    public boolean verifyRoleExists(AuthRole role) {
        List<AuthRole> tempRoles = authRoleMapper.verifyRoleExists(role);
        if (tempRoles.size() > 0) {
            for (AuthRole r : tempRoles) {
                if (!r.getId().equals(role.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<ResourceOperations> buildResourceOperations() {
        List<ResourceOperations> resourceOperationsList = new ArrayList<ResourceOperations>(0);
        List<AuthResource> resources = authPermissionMapper.getResources();
        for (AuthResource rsrc : resources) {
            ResourceOperations resourceOperation = new ResourceOperations();
            List<AuthOperation> operations = authPermissionMapper.getOperations(rsrc);
            resourceOperation.setResource(rsrc);
            resourceOperation.setOperations(operations);
            resourceOperationsList.add(resourceOperation);
        }
        return new BuildPermissionTree(resourceOperationsList).buildTree();
    }

    @Override
    public ResponseEntity<?> addRolePermission(RolePermissionVo vo, long userId) {
        Long roleId = snowflake.next();
        List<AuthRole> roles = authRoleMapper.selectByName(vo.getName());
        if (roles.size() > 0) {
            return ResponseError.create("添加失败. 角色已存在.");
        } else {
            AuthRole role = new AuthRole();
            role.setId(roleId);
            role.setCode(vo.getCode());
            role.setName(vo.getName());
            role.setDescr(vo.getDescr());
            role.setStatus(vo.getStatus());
            role.setCreateTime(new Date());
            role.setUpdateTime(new Date());
            role.setCreateBy(userId);
            authRoleMapper.insert(role);
            if (vo.getPermissionVos().size() > 0) {
                authPermissionMapper.insertPermissionBatch(getPermissionList(roleId, vo));
            }
            return ResponseOk.create("新增成功.");
        }
    }

    public List<AuthPermission> getPermissionList(Long roleId, RolePermissionVo vo) {
        List<AuthPermission> resultList = new ArrayList<AuthPermission>(0);
        List<PermissionVo> voList = vo.getPermissionVos();
        for (PermissionVo perVo : voList) {
            String[] ops = perVo.getOps().split(",");
            for (String opId : ops) {
                AuthPermission temp = new AuthPermission();
                temp.setRoleId(roleId);
                temp.setResourceId(perVo.getResourceId());
                temp.setOperationId(Long.parseLong(opId));
                resultList.add(temp);
            }
        }
        return resultList;
    }

    @Override
    public ResponseEntity<?> editRolePermission(RolePermissionVo vo, long userId) {
        AuthRole role = new AuthRole();
        role.setId(vo.getId());
        role.setCode(vo.getCode());
        role.setName(vo.getName());
        role.setDescr(vo.getDescr());
        role.setStatus(vo.getStatus());
        role.setUpdateTime(new Date());
        role.setUpdateBy(userId);
        authRoleMapper.updateByRoleId(role);
        authPermissionMapper.deletePermissions(role);
        if (vo.getPermissionVos().size() > 0) {
            authPermissionMapper.insertPermissionBatch(getPermissionList(vo.getId(), vo));
        }
        return ResponseOk.create("编辑成功.");
    }


}
