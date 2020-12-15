package com.stackstech.dcp.server.auth.web;

import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.server.auth.api.ApiUrls;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登出
 */
@RestController
@RequestMapping(ApiUrls.API_AUTH_URI)
public class LogoutController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected LoginUserManager loginUserManager;

    /**
     * 用户注销
     *
     * @param response
     * @return
     */
    @PostMapping(value = ApiUrls.API_AUTH_LOGOUT_URI)
    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest req) {
        // LoginUserManager.requiresAuthenticated();
        // LoginUserProtos.LoginUser loginUser = LoginUserManager.getLoginUser();

        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                // remove loginUser info from redis
                loginUserManager.logout(loginUser.getToken());
                // SecurityUtils.getSubject().logout();
                // remove token from cookie
                setCookie(loginUser.getToken(), 0, response);
                return ResponseOk.create("result", "ok");
            } else {
                return ResponseOk.create("用户未登录");
            }
        } else {
            return ResponseOk.create("用户未登录");
        }
    }

    /**
     * 设置cookie
     *
     * @param accessToken
     * @param expires
     * @return
     */
    private void setCookie(String accessToken, int expires, HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", accessToken);
        cookie.setMaxAge(expires);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("");
        response.addCookie(cookie);
    }

}
