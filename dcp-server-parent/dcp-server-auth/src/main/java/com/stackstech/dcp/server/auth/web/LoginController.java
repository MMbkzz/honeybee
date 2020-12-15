package com.stackstech.dcp.server.auth.web;

import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.util.IPConvertUitl;
import com.stackstech.dcp.core.util.PasswordCrypt;
import com.stackstech.dcp.core.util.TokenUtil;
import com.stackstech.dcp.server.auth.api.ApiUrls;
import com.stackstech.dcp.server.auth.dao.AuthUserMapper;
import com.stackstech.dcp.server.auth.model.AuthRole;
import com.stackstech.dcp.server.auth.model.AuthUser;
import com.stackstech.dcp.server.auth.model.vo.MenuVo;
import com.stackstech.dcp.server.auth.model.vo.UserVo;
import com.stackstech.dcp.server.auth.service.RoleService;
import com.stackstech.dcp.server.auth.service.UserService;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 登入
 */
@Controller
@RequestMapping(ApiUrls.API_AUTH_URI)
public class LoginController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthUserMapper authUserMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    protected LoginUserManager loginUserManager;

    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String SSO_HEADER = "oam_uid";
    private final int cookieExpireTime = 3 * 24 * 60 * 60;

//    /**
//     * SSO单点登录
//     *
//     * @param request
//     * @param response
//     * @return
//     */
//    @GetMapping(value = ApiUrls.API_SSO_LOGIN_URI)
//    public void sso(HttpServletRequest request, HttpServletResponse response) {
//        String ssoHeader = request.getHeader(SSO_HEADER);
//        try {
//            UserVo userVo = new UserVo();
//            userVo.setLdapUser(ssoHeader);
//            AuthUser authUser = authUserMapper.selectByLdapName(userVo);
//            if (authUser == null) {
//                AuthResponseVo vo = AuthResponseVo.error("用户不存在");
//                response.sendRedirect(frontUri + "?info=" + URLEncoder.encode(Base64.encodeToString(JacksonUtil.beanToJson(vo).getBytes(StandardCharsets.UTF_8)), "UTF-8"));
//                return;
//            }
//            if ("0".equals(authUser.getStatus())) {
//                AuthResponseVo vo = AuthResponseVo.error("用户未启用");
//                response.sendRedirect(frontUri + "?info=" + URLEncoder.encode(Base64.encodeToString(JacksonUtil.beanToJson(vo).getBytes(StandardCharsets.UTF_8)), "UTF-8"));
//                return;
//            }
//            AuthResponseVo vo = AuthResponseVo.ok(ssoHeader);
//            response.sendRedirect(frontUri + "?info=" + URLEncoder.encode(Base64.encodeToString(JacksonUtil.beanToJson(vo).getBytes(StandardCharsets.UTF_8)), "UTF-8"));
//            return;
//        } catch (Exception e) {
//            logger.error("sso login error!", e);
//            try {
//                AuthResponseVo vo = AuthResponseVo.error(ssoHeader);
//                response.sendRedirect(frontUri + "?info=" + URLEncoder.encode(Base64.encodeToString(JacksonUtil.beanToJson(vo).getBytes(StandardCharsets.UTF_8)), "UTF-8"));
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
//    }

    /**
     * 根据ldapName返回用户信息
     *
     * @return
     */
    @GetMapping(value = ApiUrls.API_SSO_USER_GET_URI)
    public @ResponseBody
    ResponseEntity<?> getUserInfo(UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        AuthUser user = authUserMapper.selectByLdapName(userVo);
        LoginUserProtos.LoginUser loginUser = setLoginUser(user, request);

        // save loginUser to redis
        loginUserManager.login(loginUser);
        // save access_token to cookie
        setCookie(loginUser.getToken(), cookieExpireTime, response);
        // save loginInfo to DB
        user.setToken(loginUser.getToken());
        //save roles to UI
        user.setRoles(loginUser.getRoleCodes().get(0));
        List<MenuVo> menu = userService.getMenuTreeByUserId(Long.parseLong(user.getId()));
        user.setMenu(menu);
        return ResponseOk.create(user);
    }

    /**
     * 平台登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ApiUrls.API_AUTH_LOGIN_URI, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(authorizationHeader)) {
            return ResponseError.create("不存在auth请求头");
        }
        if (!authorizationHeader.startsWith("Basic")) {
            return ResponseError.create("不存在Basic");
        }
        String[] authTokens = authorizationHeader.split(" ");
        if (authTokens.length < 2) {
            return ResponseError.create("用户名或为空或者不正确");
        }
        String decoded = Base64.decodeToString(authTokens[1]);
        String[] prinCred = decoded.split(":", 2);
        if (prinCred.length != 2) {
            return ResponseError.create("token为空或者不正确");
        }
        String username = StringUtils.trimToEmpty(prinCred[0]);
        String password = StringUtils.trimToEmpty(prinCred[1]);

        UserVo userVo = new UserVo();
        userVo.setLoginName(username);
        AuthUser user = authUserMapper.selectByLoginName(userVo);
        if (user == null) {
            return ResponseError.create("未找到用户");
        }
        // 判断用户是否启用
        if ("0".equals(user.getStatus())) {
            return ResponseError.create("用户未启用: " + username);
        }
        if (!user.getPassword().equals(PasswordCrypt.encode(password, user.getPasswdSalt(), "new"))) {
            return ResponseError.create("密码不正确");
        }

        LoginUserProtos.LoginUser loginUser = setLoginUser(user, request);
        // save loginUser to redis
        loginUserManager.login(loginUser);
        // save access_token to cookie
        setCookie(loginUser.getToken(), cookieExpireTime, response);
        // save loginInfo to DB
        user.setToken(loginUser.getToken());
        //save roles to UI
        user.setRoles(loginUser.getRoleCodes().get(0));

        // 加载菜单
        List<MenuVo> menu = userService.getMenuTreeByUserId(Long.parseLong(user.getId()));
        user.setMenu(menu);
        return ResponseOk.create(user);
    }

    /**
     * 设置cookie
     *
     * @param accessToken
     * @param expires
     * @return
     */
    private void setCookie(String accessToken, int expires, HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", accessToken);
        cookie.setMaxAge(expires);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //cookie.setDomain("");
        response.addCookie(cookie);
    }

    private LoginUserProtos.LoginUser setLoginUser(AuthUser user, HttpServletRequest request) {
        LoginUserProtos.LoginUser loginUserBuilder = new LoginUserProtos.LoginUser();
        // set loginUser info
        loginUserBuilder.setUserId(Long.parseLong(user.getId()));
        loginUserBuilder.setLoginTime(System.currentTimeMillis());
        loginUserBuilder.setLoginAgent(WebUtils.toHttp(request).getHeader("User-Agent"));
        loginUserBuilder.setLoginHost(IPConvertUitl.getIP(request));
        // 用户角色
        List<AuthRole> roles = roleService.getRoleByUser(Long.parseLong(user.getId()));
        List<String> roleNames = new ArrayList<String>();
        List<String> roleCodes = new ArrayList<>();
        for (AuthRole role : roles) {
            roleNames.add(role.getName());
            roleCodes.add(role.getCode());
        }
        loginUserBuilder.setRoleNames(roleNames);
        loginUserBuilder.setRoleCodes(roleCodes);
        loginUserBuilder.setUserName(user.getLoginName());
//		loginUserBuilder.setLoginType(String.valueOf(user.getLoginType()));
        String token = LoginUserManager.generateAccessToken(loginUserBuilder);
        System.out.println("token:" + token);
        loginUserBuilder.setToken(token);

        return loginUserBuilder;
    }

    /**
     * 登录异常
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ApiUrls.API_AUTH_LOGIN_ERROR_URI)
    public @ResponseBody
    ResponseEntity<?> loginError(HttpServletRequest request, HttpServletResponse response) {
        String error_str = request.getParameter("error_str");
        return ResponseError.create(4010, error_str);
    }

    private ResponseEntity<?> loginByCookie(HttpServletRequest request, HttpServletResponse response) {
        String token = TokenUtil.getTokenFromCookie(request);
        if (token == null) {
            return ResponseError.create("token 为空");
        }
        LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
        if (loginUser == null) {
            return ResponseError.create("获取用户信息为空");
        } else if (!token.equals(loginUser.getToken())) {
            // mathch md5
            return ResponseError.create("token不正确");
        } else {
            // update cookie
            setCookie(token, cookieExpireTime, response);
            // update loginUser to redis
            loginUserManager.login(loginUser);
            // save loginInfo to DB
            UserVo userVo = new UserVo();
            userVo.setLoginName(loginUser.getUserName());
            AuthUser user = authUserMapper.selectByloginId(loginUser.getUserId());
            user.setToken(loginUser.getToken());
            //save loginInfo to DB

            // 加载菜单
            List<MenuVo> menu = userService.getMenuTreeByUserId(Long.parseLong(user.getId()));
            user.setMenu(menu);
            return ResponseOk.create(user);
        }
    }
}
