package com.stackstech.dcp.server.auth.web;

import com.google.common.collect.Maps;
import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.util.PasswordCrypt;
import com.stackstech.dcp.server.auth.api.ApiUrls;
import com.stackstech.dcp.server.auth.dao.AuthUserMapper;
import com.stackstech.dcp.server.auth.model.AuthUser;
import com.stackstech.dcp.server.auth.model.vo.UserVo;
import com.stackstech.dcp.server.auth.service.UserService;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户
 */
@RestController
@RequestMapping(ApiUrls.API_AUTH_USER_URI)
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private LoginUserManager loginUserManager;

//    /**
//     * 获取OSP平台LDAP用户
//     *
//     * @param userVo
//     * @return
//     */
//    @GetMapping(value = ApiUrls.API_AUTH_OSP_USER_GET_URI)
//    public ResponseEntity<?> getOspUser(UserVo userVo) {
//        if (userVo == null || userVo.getLoginName() == null) {
//            return ResponseError.create(400, "用户名为空");
//        }
//        //1.校验ldap用户是否已同步
//        /*UserVo vo = new UserVo();
//        vo.setLdapUser(userVo.getLoginName());
//        AuthUser authUser = authUserMapper.selectByLdapName(vo);
//        if (authUser != null) {
//            return ResponseError.create(400,"当前ldap用户已同步");
//        }*/
//        //2.调用osp接口同步ldap用户
//        Map<String, Object> params = new HashMap<>();
//        params.put("userName", userVo.getLoginName());
//        String responseString = HttpUtil.doGet(ospUri + "v2/sys/user/getUserInfo", null, params);
//        try {
//            if (StringUtils.isNotBlank(responseString)) {
//                Map<String, Object> responseMap = JacksonUtil.convertToMaps(responseString);
//                if (responseMap != null && responseMap.get("obj") != null) {
//                    Map<String, Object> obj = (Map<String, Object>) responseMap.get("obj");
//                    if (!obj.isEmpty()) {
//                        return ResponseOk.create(obj);
//                    }
//                }
//            }
//            return ResponseError.create("当前ldap用户" + userVo.getLoginName() + "不存在");
//        } catch (ClientHandlerException e) {
//            return ResponseError.create("服务连接异常,同步ldap用户失败");
//        } catch (Exception e) {
//            return ResponseError.create("同步ldap用户失败");
//        }
//    }


    /**
     * 新增用户
     *
     * @param userVo
     * @return
     */
    @PostMapping(value = {ApiUrls.API_AUTH_USER_ADD_URI, ApiUrls.API_AUTH_USER_EDIT_URI})
    public ResponseEntity<?> addUser(@RequestBody UserVo userVo, HttpServletRequest req) {
        AuthUser user = userService.selectByLoginName(userVo.getLoginName());
        if (user != null && user.getId().equals(userVo.getId())) {
            return ResponseError.create(400, "登录名重复");
        }
        if (userVo.getId() == null || userVo.getId() == 0) {
            String salt = UUID.randomUUID().toString().replace("-", "");
            userVo.setPassword(PasswordCrypt.encode(userVo.getPassword(), salt, "new"));
            userVo.setPasswdSalt(salt);
            userVo.setUserSource("NaN");
            userVo.setStatus("1");
        }
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                return userService.addUserOrUpdate(userVo, loginUser.getUserId());
            }
        }
        return ResponseError.create(400, (StringUtils.isEmpty(userVo.getId().toString()) ? "新增" : "修改") + "失败");
    }

    /**
     * 用户启用停用接口
     *
     * @param param 用户id,status ：0 未启用 1启用
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_USER_SSU_URI)
    public ResponseEntity<?> startStopUser(@RequestBody Map<String, String> param, HttpServletRequest req) {
        if (param == null || StringUtils.isEmpty(param.get("id")) || StringUtils.isEmpty(param.get("status"))) {
            return ResponseError.create(10001, "参数不能为空");
        }
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                userService.startStopUser(param.get("id"), param.get("status"), loginUser.getUserId());
            }
        }
        return ResponseOk.create("result", "OK");
    }

    /**
     * 配置用户角色接口
     *
     * @param id
     * @param rolesId
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_USER_DEPLOY_URI)
    public ResponseEntity<?> deployUserRole(@RequestParam String id, @RequestParam String[] rolesId) {
        userService.deployUserRole(id, rolesId);
        return ResponseOk.create("result", "OK");
    }

    /**
     * 用户查询列表接口
     *
     * @param userVo
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_USER_GET_URI)
    public ResponseEntity<?> getUsers(UserVo userVo, Page page) {
        Map<String, Object> result = userService.getPageUsers(userVo, page);
        return ResponseOk.create(result);
    }

    /**
     * 查询用户详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_USER_GET_USERINFO_URI)
    public ResponseEntity<?> getUserInfo(@RequestParam Long id) {
        UserVo user = userService.getUserInfo(id);
        return ResponseOk.create(user);
    }

    /**
     * 删除用户
     *
     * @param map
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_USER_DEL_URI)
    public ResponseEntity<?> delUser(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("id");
            if (null != id && !"".equals(id)) {
                userService.delUser(Long.parseLong(id));
            }
            return ResponseOk.create("result", "OK");
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return ResponseError.create("删除用户失败");
        }
    }

    /**
     * 修改用户密码
     *
     * @param userVo
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_USER_PWD_EDIT_URI)
    public ResponseEntity<?> editPwd(@RequestBody UserVo userVo, HttpServletRequest req) {
        log.info("UserVo = [{}]", userVo.toString());
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                log.info("loginUser = [{}]", loginUser.toString());
                userVo.setId(loginUser.getUserId());
                if (StringUtils.isEmpty(userVo.getOldPassword()) || StringUtils.isEmpty(userVo.getPassword())) {
                    return ResponseError.create(400, "参数为空!");
                }

                //1. 登陆验证
                UserVo currentUser = userService.getUserInfo(loginUser.getUserId());
                currentUser.setOldPassword(userVo.getOldPassword());
                log.info("currentUser = [{}]", currentUser.toString());
                if (currentUser == null) {
                    return ResponseError.create(400, "非法用户!");
                }

                //2. 密码验证(验证+加密)
                String oldPwd = PasswordCrypt.encode(currentUser.getOldPassword(), currentUser.getPasswdSalt(), "new");
                if (!StringUtils.equals(currentUser.getPassword(), oldPwd)) {
                    return ResponseError.create(400, "旧密码错误!");
                }
                String salt = UUID.randomUUID().toString().replace("-", "");
                userVo.setPassword(PasswordCrypt.encode(userVo.getPassword(), salt, "new"));
                userVo.setPasswdSalt(salt);
                //3.更新密码 并且删除登陆日志
                if (loginUser != null) {
                    userService.editPwd(userVo, loginUser.getUserId());
                }
                return ResponseOk.create("result", "OK");
            } else {
                return ResponseOk.create("用户未登录");
            }
        } else {
            return ResponseOk.create("用户未登录");
        }
    }


    /**
     * 查询操作人上级ID
     *
     * @param userId
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_USER_LEADER_URI)
    public ResponseEntity<?> getUserLeader(@RequestParam(required = true, name = "userId") Long userId) {
        return ResponseOk.create(null);
    }

    @GetMapping(value = ApiUrls.API_AUTH_USERS_NAME_CHECK_URI)
    public ResponseEntity<?> checkUsername(@RequestParam("name") String
                                                   name, @RequestParam(value = "id", required = false)
                                                   Long id) {
        Boolean exists = null;
        if (userService.selectByName(name, id) != null) {
            exists = Boolean.TRUE;
        } else {
            exists = Boolean.FALSE;
        }
        Map<String, Boolean> result = Maps.newHashMap();
        result.put("exist", exists);
        return ResponseOk.create(result);
    }

    @GetMapping(value = ApiUrls.API_AUTH_USERS_LOGINNAME_CHECK_URI)
    public ResponseEntity<?> checkLoginName(@RequestParam("name") String name, @RequestParam(value = "id", required = false) Long id) {
        Boolean exists = null;
        AuthUser user = userService.selectByLoginName(name);
        if (user != null && user.getId().equals(id)) {
            exists = Boolean.TRUE;
        } else {
            exists = Boolean.FALSE;
        }
        Map<String, Boolean> result = Maps.newHashMap();
        result.put("exist", exists);
        return ResponseOk.create(result);
    }

    @GetMapping(value = ApiUrls.API_AUTH_USERS_EMAIL_CHECK_URI)
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email, @RequestParam(value = "id", required =
            false) Long id) {
        Boolean exists = null;
        if (userService.selectByEmail(email, id) != null) {
            exists = Boolean.TRUE;
        } else {
            exists = Boolean.FALSE;
        }
        Map<String, Boolean> result = Maps.newHashMap();
        result.put("exist", exists);
        return ResponseOk.create(result);
    }

    @GetMapping(value = ApiUrls.API_AUTH_USERS_PHONE_CHECK_URI)
    public ResponseEntity<?> checkPhone(@RequestParam("phone") String phone, @RequestParam(value = "id", required =
            false) Long id) {
        Boolean exists = null;
        if (userService.selectByPhone(phone, id) != null) {
            exists = Boolean.TRUE;
        } else {
            exists = Boolean.FALSE;
        }
        Map<String, Boolean> result = Maps.newHashMap();
        result.put("exist", exists);
        return ResponseOk.create(result);
    }

    /**
     * 获取角色下会员列表
     *
     * @param roleCode 角色编号
     * @return
     */
    @GetMapping(value = ApiUrls.API_AUTH_ROLE_GET_ROLE_USER_URI)
    public ResponseEntity<?> getRoleUserList(@RequestParam String roleCode) {
        List<Map<String, Object>> roleUserList = userService.getRoleUserList(roleCode);
        return ResponseOk.create(roleUserList);
    }

}
