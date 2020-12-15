package com.stackstech.dcp.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

    public static String getTokenFromCookie(HttpServletRequest request) {
        if ((request instanceof HttpServletRequest) == false) {
            return null;
        }
        HttpServletRequest req = request;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("access_token".equals(c.getName())) {
                    return StringUtils.trimToNull(c.getValue());
                }
            }
        }
        return null;
    }

    public static String getHost(HttpServletRequest request) {
        String host = WebUtils.toHttp(request).getHeader("x-forwarded-for");
        if (host == null || host.isEmpty()) {
            return request.getRemoteHost();
        } else {
            return host;
        }
    }

    public static String getAgent(HttpServletRequest request) {
        return WebUtils.toHttp(request).getHeader("User-Agent");
    }
}
