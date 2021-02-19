package com.stackstech.honeybee.server.core.inteceptor;

import com.stackstech.honeybee.common.utils.AuthTokenBuilder;
import com.stackstech.honeybee.server.core.annotation.ApiAuthIgnore;
import com.stackstech.honeybee.server.core.enums.HttpHeader;
import com.stackstech.honeybee.server.core.enums.TokenStatus;
import com.stackstech.honeybee.server.core.exception.AuthenticationException;
import com.stackstech.honeybee.server.core.handler.MessageHandler;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import com.stackstech.honeybee.server.system.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
        HandlerMethod handlerMethod;
        try {
            if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
                response.setStatus(HttpStatus.OK.value());
                return true;
            }
            handlerMethod = (HandlerMethod) handler;
        } catch (Exception e) {
            log.error("request method not exist, return 404.", e);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.getWriter().print(HttpStatus.NOT_FOUND.getReasonPhrase());
            return false;
        }
        Method method = handlerMethod.getMethod();
        ApiAuthIgnore authIgnore = method.getAnnotation(ApiAuthIgnore.class);
        if (null != authIgnore) {
            log.info("API authentication ignore {}", request.getRequestURI());
            return true;
        }
        // verify the token exists
        String token = request.getHeader(HttpHeader.AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationException(MessageHandler.of().message("auth.token.empty"));
        }
        TokenStatus status = authTokenBuilder.verifyToken(token);
        if (status == TokenStatus.INVALID) {
            throw new AuthenticationException(MessageHandler.of().message("auth.token.invalid"));
        }
        AccountEntity account = authService.verifyAccount(token);
        if (status == TokenStatus.EXPIRES) {
            log.debug("The authentication token expires, Reissue the authentication token to the client");
            authTokenBuilder.refreshAuthToken(token, response);
        }
        request.setAttribute(HttpHeader.REQUEST_ACCOUNT, account);
        return true;
    }

}
