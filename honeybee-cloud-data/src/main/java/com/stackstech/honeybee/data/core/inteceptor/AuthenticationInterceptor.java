package com.stackstech.honeybee.data.core.inteceptor;


import com.stackstech.honeybee.data.core.annotation.ApiAuthIgnore;
import com.stackstech.honeybee.data.core.enums.HttpHeader;
import com.stackstech.honeybee.data.core.enums.StatusCode;
import com.stackstech.honeybee.data.core.enums.TokenStatus;
import com.stackstech.honeybee.data.service.AuthService;
import com.stackstech.honeybee.data.utils.AuthTokenBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author william
 */
@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthTokenBuilder authTokenBuilder;
    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = null;
        try {
            if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
                response.setStatus(StatusCode.SUCCESS.getHttpCode());
                return true;
            }
            handlerMethod = (HandlerMethod) handler;
        } catch (Exception e) {
            log.error("request method not exist, return 404.", e);
            response.setStatus(StatusCode.NOT_FOUND.getHttpCode());
            response.getWriter().print(StatusCode.NOT_FOUND.getMessage());
            return false;
        }
        Method method = handlerMethod.getMethod();
        ApiAuthIgnore authIgnore = method.getAnnotation(ApiAuthIgnore.class);
        if (handler instanceof HandlerMethod && null != authIgnore) {
            log.info("API authentication ignore {}", request.getRequestURI());
            return true;
        }
        // verify the token exists
        String token = request.getHeader(HttpHeader.AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            response.setStatus(StatusCode.UNAUTHORIZED.getHttpCode());
            response.getWriter().print(StatusCode.UNAUTHORIZED.getMessage());
            return false;
        }
        TokenStatus status = authTokenBuilder.verifyToken(token);
        if (status == TokenStatus.INVALID) {
            response.setStatus(StatusCode.UNAUTHORIZED.getHttpCode());
            response.getWriter().print(StatusCode.UNAUTHORIZED.getMessage());
            return false;
        }
        if (!authService.verifyAccount(token)) {
            response.setStatus(StatusCode.UNAUTHORIZED.getHttpCode());
            response.getWriter().print(StatusCode.UNAUTHORIZED.getMessage());
            return false;
        }
        if (status == TokenStatus.EXPIRES) {
            log.debug("The authentication token expires, Reissue the authentication token to the client");
            authTokenBuilder.refreshAuthToken(token, response);
        }
        return true;
    }

}
