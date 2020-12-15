package com.stackstech.dcp.server.auth.utils;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

    public static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    public static String getToken(ServletRequest request) {
        String token = null;
        if (token == null) {
            token = TokenUtil.getTokenFromParameter(request);
            logger.debug("getTokenFromParameter: {}.", token);
        }
        if (token == null) {
            token = TokenUtil.getTokenFromHeader(request);
            logger.debug("getTokenFromHeader: {}.", token);
        }
        if (token == null) {
            token = TokenUtil.getTokenFromCookie(request);
            logger.debug("getTokenFromCookie: {}.", token);
        }
        return token;
    }

    public static String getTokenFromCookie(ServletRequest request) {
        if ((request instanceof HttpServletRequest) == false) {
            return null;
        }
        HttpServletRequest req = (HttpServletRequest) request;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("access_token".equals(c.getName())) {
                    return trimToNull(c.getValue());
                }
            }
        }
        return null;
    }

    public static String getTokenFromHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String authzHeader = httpRequest.getHeader("Authorization");
        if (authzHeader == null || authzHeader.length() == 0) {
            return null;
        }
        if (!authzHeader.startsWith("Token") || authzHeader.length() < 6) {
            return null;
        }
        String token = authzHeader.substring(5);
        return trimToNull(token);
    }

    public static String getTokenFromParameter(ServletRequest request) {
        String token = request.getParameter("access_token");
        if (token == null) {
            return null;
        }
        return trimToNull(token);
    }

    public static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (s.length() > 0) {
            return s;
        } else {
            return null;
        }
    }

}
