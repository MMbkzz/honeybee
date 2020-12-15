package com.stackstech.dcp.server.system.filter;

import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.operations.dao.SysAuditLogMapper;
import com.stackstech.dcp.server.operations.model.SysAuditLog;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * 日志过滤器
 */
@WebFilter(filterName = "sysAuditLogFilter", urlPatterns = "/api/*")
public class SysAuditLogFilter implements Filter {

    @Autowired
    private LoginUserManager loginUserManager;
    @Autowired
    private SysAuditLogMapper sysAuditLogMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        HttpServletResponse resp = (HttpServletResponse) response;
        ResponseWrapper wrapper = new ResponseWrapper(resp);
        HttpServletRequest req = (HttpServletRequest) request;

        //登录URL+下载URL拦截
        String requestUri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String url = requestUri.substring(contextPath.length());

        String[] urls = {"/api/auth/sso", "/api/auth/get_ldap_user", "/api/auth/login", "/api/auth/error", "/api/services/download_service"};
        if (ArrayUtils.contains(urls, url)) {
            chain.doFilter(request, response);
            return;
        }

        String token = TokenUtil.getToken(req);
        //没有token,返回登录
        if (token == null) {
            req.getRequestDispatcher("/api/auth/error?error_str=用户未登录").forward(request, response);
            return;
        }
        //获取不到用户信息,返回登录
        LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
        if (loginUser == null) {
            req.getRequestDispatcher("/api/auth/error?error_str=用户登录已过期").forward(request, response);
            return;
        }
        long endTime = System.currentTimeMillis();

        SysAuditLog log = new SysAuditLog();
        log.setUserId(loginUser.getUserId());
        log.setApi(req.getServletPath());
        log.setApiDesc("");
        log.setIp(req.getRemoteAddr());
        log.setStatus((wrapper.getStatus() != 0 ? String.valueOf(wrapper.getStatus()) : "500"));
        log.setCreateBy(loginUser.getUserId());
        log.setRequestTime(new Timestamp(startTime));
        log.setResponseTime((endTime - startTime) > 0 ? (endTime - startTime) : 0);
        sysAuditLogMapper.insert(log);

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }

}
