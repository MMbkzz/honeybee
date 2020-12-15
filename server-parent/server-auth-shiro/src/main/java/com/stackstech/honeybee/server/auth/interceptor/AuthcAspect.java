package com.stackstech.honeybee.server.auth.interceptor;

import com.stackstech.honeybee.server.auth.utils.AuthTokenUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class AuthcAspect {

    @Pointcut("@annotation(com.stackstech.dcp.server.auth.annotation.Authc)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = new ServletWebRequest(request).getResponse();

        AuthenticationToken token = AuthTokenUtil.createAuthenticationToken(request, response);
        if (null == token) {
            throw new AuthenticationException();
        }

        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
    }

}
