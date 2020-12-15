package com.stackstech.honeybee.server.auth.shiro;

import com.stackstech.honeybee.core.model.LoginUserProtos;
import com.stackstech.honeybee.server.auth.utils.LoginUserManager;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class RedisAuthenticatingRealm extends AuthorizingRealm {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private LoginUserManager loginUserManager;

    public RedisAuthenticatingRealm() {
        super();
        // 登陆信息保存在Redis中就不需要Cache了
        super.setCachingEnabled(false);
    }

    public void setLoginUserManager(LoginUserManager loginUserManager) {
        this.loginUserManager = loginUserManager;
    }

    public LoginUserManager getLoginUserManager() {
        return loginUserManager;
    }

    private SimpleAccount getSimpleAccount(LoginUserProtos.LoginUser loginUser) {
        logger.debug("getSimpleAccount {}.", loginUser);
        Set<String> roleNames = new HashSet<>(loginUser.getRoleNames().size());
        roleNames.addAll(loginUser.getRoleNames());
        Set<Permission> permissions = new HashSet<>(loginUser.getPermissions().size());
        for (String permission : loginUser.getPermissions()) {
            permissions.add(new WildcardPermission(permission));
        }
        return new SimpleAccount(loginUser, loginUser.getToken(), "Redis", roleNames, permissions);
    }

    /**
     * 通过token获取用户信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AuthAccessToken tokenToken = (AuthAccessToken) token;
        logger.debug("doGetAuthenticationInfo {}.", tokenToken);
        return getSimpleAccount(tokenToken);
    }

    private AuthenticationInfo getSimpleAccount(AuthAccessToken token) {
        String principal = (String) token.getPrincipal();
        logger.debug("getSimpleAccount principal: {}.", principal);
        LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(principal);
        logger.debug("getSimpleAccount loginUser: {}.", loginUser);
        if (loginUser == null) {
            throw new AuthenticationException("Illegal Principal [" + principal + "]");
        }
        String host = token.getHost();
        logger.debug("getSimpleAccount host: {}.", host);
        if (!equals(host, loginUser.getLoginHost())) {
            throw new AuthenticationException("Illegal host.");
        }
        String agent = token.getAgent();
        logger.debug("getSimpleAccount agent: {}.", agent);
        if (!equals(agent, loginUser.getLoginAgent())) {
            throw new AuthenticationException("Illegal agent.");
        }
        return getSimpleAccount(loginUser);
    }

    private boolean equals(String cs1, String cs2) {
        if (cs1 != null && cs1.length() == 0) {
            cs1 = null;
        }
        if (cs2 != null && cs2.length() == 0) {
            cs2 = null;
        }
        return cs1 == null ? cs2 == null : cs1.equals(cs2);
    }

    /**
     * 通过用户信息获取权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return getSimpleAccount(((LoginUserProtos.LoginUser) principals.getPrimaryPrincipal()));
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthAccessToken;
    }

}
