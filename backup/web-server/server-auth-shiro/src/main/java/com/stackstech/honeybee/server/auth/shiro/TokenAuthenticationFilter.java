package com.stackstech.honeybee.server.auth.shiro;

import com.stackstech.honeybee.core.util.IPConvertUitl;
import com.stackstech.honeybee.server.auth.utils.TokenUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationFilter extends AuthenticatingFilter {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected AuthenticationToken createToken(ServletRequest request,
                                              ServletResponse response) throws Exception {
        String token = TokenUtil.getToken(request);
        logger.debug("Authentication token {}.", token);
        if (token == null || token.length() == 0) {
            return null;
        }
        String host = getHost(request);
        logger.debug("Authentication token host{}.", host);
        String agent = getAgent(request);
        // 把用戶名和密码一样
        return new AuthAccessToken(token, host, agent);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {
        boolean loggedIn = false;
        if (isLoginAttempt(request, response)) {
            loggedIn = executeLogin(request, response);
        }
        if (!loggedIn) {
            sendChallenge(request, response);
        }
        return loggedIn;
    }

    protected boolean isLoginAttempt(ServletRequest request,
                                     ServletResponse response) {
        String token = TokenUtil.getToken(request);
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request,
                                   ServletResponse response) throws Exception {
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            return false;
        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    @Override
    protected String getHost(ServletRequest request) {
        return IPConvertUitl.getIP((HttpServletRequest) request);
    }

    private String getAgent(ServletRequest request) {
        return WebUtils.toHttp(request).getHeader("User-Agent");
    }

    protected boolean sendChallenge(ServletRequest request,
                                    ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

}
