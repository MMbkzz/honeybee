package com.stackstech.dcp.server.auth.shiro;

import com.stackstech.dcp.core.util.IPConvertUitl;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class TokenAccessControlFilter extends PathMatchingFilter {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.FALSE);
        return true;
    }

    @Deprecated
    private AuthenticationToken createAuthenticationToken(ServletRequest request, ServletResponse response) {
        String token = TokenUtil.getToken(request);
        logger.debug("getToken: {}.", token);
        if (token == null || token.length() == 0) {
            logger.debug("createToken token is null.");
            return null;
        }
        String host = IPConvertUitl.getIP((HttpServletRequest) request);
        String agent = getAgent(request);
        logger.debug("createToken, token {}, host {}, agent {}.", token, host, agent);
        // 把用戶名和密码一样
        return new AuthAccessToken(token, host, agent);
    }

    private String getAgent(ServletRequest request) {
        return WebUtils.toHttp(request).getHeader("User-Agent");
    }

}
