package com.stackstech.dcp.server.dataservice.web;


import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.dataservice.api.ApiUrls;
import com.stackstech.dcp.server.dataservice.model.AppUser;
import com.stackstech.dcp.server.dataservice.service.AppUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据服务APP用户
 */
@RestController
@RequestMapping(ApiUrls.API_SERVICE_URI)
public class AppUserController {

    private final Logger log = LoggerFactory.getLogger(AppUserController.class);

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private LoginUserManager loginUserManager;

    /**
     * 新增数据服务APP用户接口
     *
     * @param appUser
     * @return
     */
    @PostMapping(value = {ApiUrls.API_SERVICE_USER_ADD_URI, ApiUrls.API_SERVICE_USER_UPDATE_URI})
    public ResponseEntity<?> addAppUser(@RequestBody AppUser appUser, HttpServletRequest req) {
        String messageTop = StringUtils.isEmpty(appUser.getId()) ? "新增" : "修改";
        try {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(TokenUtil.getToken(req));
            int i = appUserService.addOrUpdate(appUser, loginUser.getUserId());
            if (i > 0) {
                return ResponseOk.create("result", messageTop + "数据服务API用户成功");
            }
        } catch (Exception e) {
            log.error(messageTop + "数据服务APP用户失败", e);
            return ResponseError.create(500, messageTop + "数据服务APP用户失败");
        }
        return ResponseError.create(500, messageTop + "数据服务API用户失败");
    }

    /**
     * 数据服务APP用户查询列表接口
     *
     * @param appUser
     * @param page
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_USER_QUERY_URI)
    public ResponseEntity<?> queryAppUsers(AppUser appUser, Page page) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (page != null && page.getPageNo() != null) {
                PageUtil.page(page);
            }
            List<Map<String, Object>> appUsers = appUserService.queryAll(appUser);
            int countAll = appUserService.countAll(appUser);
            map.put("list", appUsers);
            map.put("count", countAll);
        } catch (Exception e) {
            log.error("获取数据服务APP用户列表失败", e);
            return ResponseError.create("获取数据服务APP用户列表失败");
        }
        return ResponseOk.create(map);
    }


    /**
     * 数据服务APP用户查询列表接口
     *
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_USER_QUERY_AUTH_URI)
    public ResponseEntity<?> queryAuthAppUsers(String dataServiceId) {
        List<AppUser> result = null;
        try {
            result = appUserService.queryUsers(dataServiceId);
        } catch (Exception e) {
            log.error("获取数据服务APP用户列表失败", e);
            return ResponseError.create("获取数据服务APP用户列表失败");
        }
        return ResponseOk.create(result);
    }

    /**
     * 查询数据数据服务APP用户详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = ApiUrls.API_SERVICE_USER_GET_URI)
    public ResponseEntity<?> getAppUser(@RequestParam String id) {
        AppUser AppUser = null;
        try {
            AppUser = appUserService.query(id);
        } catch (Exception e) {
            log.error("获取数据服务APP用户失败", e);
            return ResponseError.create("获取数据服务APP用户失败");
        }
        return ResponseOk.create(AppUser);
    }

    /**
     * 编辑数据服务APP用户
     *
     * @param appUser
     * @return
     */
//    @PostMapping(value = ApiUrls.API_SERVICE_USER_UPDATE_URI)
//    public ResponseEntity<?> editAppUser(@RequestBody AppUser appUser, HttpServletRequest req) {
//        try {
//            int i = appUserService.update(appUser, req);
//            if (i <= 0) {
//                return ResponseError.create("更新数据服务APP用户失败");
//            }
//        } catch (Exception e) {
//            log.error("更新数据服务APP用户失败", e);
//            return ResponseError.create("更新数据服务APP用户失败");
//        }
//        return ResponseOk.create("更新数据服务APP用户成功");
//    }

    /**
     * 删除数据服务APP用户
     *
     * @param map
     * @return
     */
    @PostMapping(value = ApiUrls.API_SERVICE_USER_DELETE_URI)
    public ResponseEntity<?> delAppUser(@RequestBody Map<String, Object> map) {
        try {
            String id = (String) map.get("id");
            if (null != id && !"".equals(id)) {
                return appUserService.delete(id);
            } else {
                return ResponseError.create(400, "请求参数为空");
            }
        } catch (Exception e) {
            log.error("删除数据服务APP用户失败", e);
            return ResponseError.create("删除数据服务APP用户失败");
        }
    }


    /**
     * 重名校验
     *
     * @param name
     * @return
     */
    @GetMapping(ApiUrls.API_SERVICE_USER_CHECK_URI)
    public ResponseEntity checkAppUser(String name, String userId) {
        try {
            AppUser appUser = appUserService.queryByName(name);
            if (appUser != null && !userId.equals(appUser.getId())) {
                return ResponseError.create(500, "服务APP已经存在!");
            }
        } catch (Exception e) {
            log.error("服务APP校验失败..", e);
            return ResponseError.create(500, "服务APP校验失败");
        }
        return ResponseOk.create("服务APP不存在!");
    }

    /**
     * 服务授权
     *
     * @param map
     * @return
     */
    @PostMapping(ApiUrls.API_SERVICE__USER_AUTHORIZATION_URI)
    public ResponseEntity<?> authAppUser(@RequestBody Map<String, Object> map) {
        if (map == null) {
            return ResponseError.create(400, "请求参数为空");
        }
        try {
            boolean flag = appUserService.auth(map);
            if (!flag) {
                return ResponseError.create(500, "服务APP授权失败");
            }
        } catch (Exception e) {
            log.error("服务授权失败", e);
            return ResponseError.create(500, "服务APP授权失败");
        }
        return ResponseOk.create("服务APP授权成功!");
    }
}
