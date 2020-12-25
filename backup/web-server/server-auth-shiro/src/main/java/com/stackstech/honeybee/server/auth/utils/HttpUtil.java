package com.stackstech.honeybee.server.auth.utils;

import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;

public class HttpUtil {

    public static String getHost(ServletRequest request) {
        String host = WebUtils.toHttp(request).getHeader("X-Forwarded-For");
        if (host == null || host.isEmpty()) {
            return request.getRemoteHost();
        } else {
            return host;
        }
    }

    public static String getAgent(ServletRequest request) {
        return WebUtils.toHttp(request).getHeader("User-Agent");
    }
}
