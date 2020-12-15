package com.stackstech.dcp.server.auth.utils;

import com.stackstech.dcp.server.auth.shiro.AuthAccessToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AuthTokenUtil {

    final static Logger logger = LoggerFactory.getLogger(AuthTokenUtil.class);

    public static AuthenticationToken createAuthenticationToken(ServletRequest request, ServletResponse response) {
        String token = TokenUtil.getToken(request);
        logger.debug("getToken: {}.", token);
        if (token == null || token.length() == 0) {
            logger.debug("createToken token is null.");
            return null;
        }
        String host = HttpUtil.getHost(request);
        String agent = HttpUtil.getAgent(request);
        logger.debug("createToken, token {}, host {}, agent {}.", token, host, agent);
        // 把用戶名和密码一样
        return new AuthAccessToken(token, host, agent);
    }

}
