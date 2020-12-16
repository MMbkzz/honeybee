package com.stackstech.honeybee.server.auth.web;

import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.core.model.LoginUserProtos;
import com.stackstech.honeybee.core.page.Page;
import com.stackstech.honeybee.server.auth.api.ApiUrls;
import com.stackstech.honeybee.server.auth.model.AuthRole;
import com.stackstech.honeybee.server.auth.model.vo.RolePermissionVo;
import com.stackstech.honeybee.server.auth.service.RoleService;
import com.stackstech.honeybee.server.auth.utils.LoginUserManager;
import com.stackstech.honeybee.server.auth.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 角色权限 Controller
 */
@RestController
@RequestMapping(ApiUrls.API_AUTH_ROLE_URI)
public class RoleController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleService roleService;

    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 新增角色接口
     *
     * @param role
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_ROLE_ADD_URI)
    public ResponseEntity<?> addRole(@RequestBody AuthRole role, HttpServletRequest req) {
        try {
            String token = TokenUtil.getToken(req);
            if (token != null) {
                LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
                if (loginUser != null) {
                    roleService.addRole(role, loginUser.getUserId());
                }
            }
        } catch (Exception e) {
            log.error("新增角色失败", e);
            return ResponseError.create(400, "新增角色失败");
        }
        return ResponseOk.create("result", "OK");
    }

    /**
     * 删除角色接口
     *
     * @param map
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_ROLE_DEL_URI)
    public ResponseEntity<?> delRole(@RequestBody Map<String, Object> map) {
        String msg = null;
        String id = (String) map.get("id");
        if (null != id && !"".equals(id)) {
            msg = roleService.delRole(Long.parseLong(id));
        }
        if (!"success".equals(msg)) {
            return ResponseError.create(msg);
        }
        return ResponseOk.create("result", "OK");
    }

    /**
     * 编辑角色接口
     *
     * @param role 角色的属性 和这个角色下的权限
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_ROLE_EDIT_URI)
    public ResponseEntity<?> editRole(@RequestBody AuthRole role, HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                roleService.editRole(role, loginUser.getUserId());
                return ResponseOk.create("result", "OK");
            }
        }
        return ResponseError.create(400, "修改失败");
    }

    /**
     * 角色启用停用接口
     *
     * @param param id 角色编号,status 0 未启用 1启用
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_ROLE_SSR_URI)
    public ResponseEntity<?> startStopRole(@RequestBody Map<String, String> param, HttpServletRequest req) {
        try {
            String token = TokenUtil.getToken(req);
            if (token != null) {
                LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
                if (loginUser != null) {
                    roleService.startStopRole(Long.parseLong(param.get("id")), param.get("status"), loginUser.getUserId());
                }
            }
        } catch (Exception e) {
            log.error("启用停用失败", e);
            return ResponseError.create(400, "启用停用失败");
        }
        return ResponseOk.create("result", "OK");
    }

    /**
     * 查询所有启用的角色信息
     *
     * @param status
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_ROLE_ALL_URI)
    public ResponseEntity<?> selectAllRole(String status) {
        try {
            return ResponseOk.create(roleService.selectAllRole(status));
        } catch (Exception e) {
            log.error("查询所有角色信息失败", e);
            return ResponseError.create("查询所有角色信息失败");
        }
    }

    /**
     * 查询角色列表
     *
     * @param vo
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_ROLE_GET_URI)
    public ResponseEntity<?> getPageRoles(RolePermissionVo vo, Page page) {
        return ResponseOk.create(roleService.getPageRoles(vo, page));
    }

    /**
     * 角色详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_ROLE_GET_USERINFO_URI)
    public ResponseEntity<?> getRoleInfo(String id) {
        if (StringUtils.isNotEmpty(id)) {
            return ResponseOk.create(roleService.getRoleInfo(Long.parseLong(id)));
        }
        return ResponseError.create(10004, "角色编号不能为空");
    }

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_USER_ROLE_URI)
    public ResponseEntity<?> getRoleByUser(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return ResponseError.create(10004, "用户编号不能为空");
        }
        return ResponseOk.create(roleService.getRoleByUser(Long.parseLong(userId)));
    }

    /**
     * 校验角色是否存在
     *
     * @param role
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_ROLE_VERIFY_EXISTS_URI)
    public ResponseEntity<?> verifyRoleExists(@RequestBody AuthRole role) {
        return ResponseOk.create(roleService.verifyRoleExists(role));
    }

    /**
     * 获取资源树
     *
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_ROLE_BUILD_RESOURCE_OPERATIONS_URI)
    public ResponseEntity<?> buildResourceOperations() {
        return ResponseOk.create(roleService.buildResourceOperations());
    }

    /**
     * 新增角色和权限
     *
     * @param vo
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_ROLE_ADD_ROLE_PERMISSION_URI)
    public ResponseEntity<?> addRolePermission(@RequestBody RolePermissionVo vo, HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                return roleService.addRolePermission(vo, loginUser.getUserId());
            }
        }
        return ResponseError.create(400, "新增失败");
    }

    /**
     * 编辑角色和权限
     *
     * @param vo
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_ROLE_EDIT_ROLE_PERMISSION_URI)
    public ResponseEntity<?> editRolePermission(@RequestBody RolePermissionVo vo, HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                return roleService.editRolePermission(vo, loginUser.getUserId());
            }
        }
        return ResponseError.create(400, "新增失败");
    }

}
