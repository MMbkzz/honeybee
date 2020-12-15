package com.stackstech.dcp.server.auth.utils;

import com.stackstech.dcp.core.cache.LoginUserProtosCache;
import com.stackstech.dcp.core.model.LoginUserProtos;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

/**
 * 登陆用户使用的方法
 */
@Component
public class LoginUserManager {

    final static Logger logger = LoggerFactory.getLogger(LoginUserManager.class);

    private final long keyExpireSecond = 7 * 24 * 60 * 60 * 1000;

    @Autowired
    private LoginUserProtosCache loginUserProtosCache;

    /**
     * 用户登录
     *
     * @param loginUser
     */
    public void login(LoginUserProtos.LoginUser loginUser) {
        String token = loginUser.getToken();
        System.out.println("set token to redis:" + token);

        loginUserProtosCache.add(token, loginUser);
        loginUserProtosCache.expire(token, keyExpireSecond);
    }

    /**
     * 根据token获取登陆信息
     *
     * @param token
     * @return
     */
    public LoginUserProtos.LoginUser getLoginUser(String token) {
        LoginUserProtos.LoginUser loginUser = loginUserProtosCache.get(token);
        if (null != loginUser) {
            loginUserProtosCache.expire(token, keyExpireSecond);
        }
        return loginUser;
    }

    /**
     * 用户注销
     *
     * @param token
     */
    public void logout(String token) {
        loginUserProtosCache.delete(token);
    }

    /**
     * 生成access_token
     *
     * @return
     */
    public static String generateAccessToken(LoginUserProtos.LoginUser user) {
        return UUID.randomUUID().toString() + "."
                + new Md5Hash(loginUsertoString(user)).toString();
    }

    /**
     * 获取登陆用户
     *
     * @return
     */
    public static LoginUserProtos.LoginUser getLoginUser() {
        LoginUserProtos.LoginUser loginUser = (LoginUserProtos.LoginUser) SecurityUtils.getSubject().getPrincipal();
        logger.debug("get loginUser {}", loginUser);
        return loginUser;
    }

    /**
     * 检查登录用户token
     *
     * @return
     */
    public static boolean checkLoginUsertoken(LoginUserProtos.LoginUser user, String token) {
        return new Md5Hash(loginUsertoString(user)).toString().equals(token.substring(36));
    }

    private static String loginUsertoString(LoginUserProtos.LoginUser user) {
        StringBuffer result = new StringBuffer();
        result.append("LoginUser [userId=").append(user.getUserId())
                .append(", loginTime=").append(user.getLoginTime())
                .append(", Agent=").append(user.getLoginAgent())
                .append(", host=").append(user.getLoginHost())
                .append(", roleNames=").append(user.getRoleNames() + "]");
        return result.toString();
    }

    public static void requiresAllRoles(String... roles) {
        SecurityUtils.getSubject().checkRoles(Arrays.asList(roles));
    }

    public static void requiresAnyRoles(String... roles) {
        for (String role : roles) {
            if (SecurityUtils.getSubject().hasRole(role)) {
                return;
            }
        }
        //抛出异常或者返回错误json
        throw new AuthorizationException("LoginUser do not has any role !");
    }

    public static void requiresAuthenticated() {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            throw new AuthenticationException("loginUser do not login!");
        }
    }

    public static void requiresPermissions(String... permissions) {
        SecurityUtils.getSubject().checkPermissions(permissions);
    }

}
