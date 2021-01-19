package com.stackstech.honeybee.server.security;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.entity.AccountEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.*;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.core.utils.AuthTokenBuilder;
import com.stackstech.honeybee.server.core.utils.CacheUtil;
import com.stackstech.honeybee.server.core.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * authentication service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private DataService<AccountEntity> accountService;
    @Autowired
    private AuthTokenBuilder authTokenBuilder;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @ApiOperation(value = "account login")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.LOGIN)
    @RequestMapping(value = "/security/login", method = RequestMethod.POST)
    public ResponseMap<?> login(@RequestBody RequestParameter parameters) {
        ResponseMap responseMap = ResponseMap.failed("login failed, please check your account and password");
        Map<String, Object> map = Maps.newHashMap();
        map.put("account", Optional.ofNullable(parameters.get("account")).orElse("default"));
        map.put("password", Optional.ofNullable(parameters.get("password")).orElse("default"));

        if (accountService.getTotalCount(map) == 1) {
            AccountEntity entity = accountService.get(map).get(0);
            // generate auth token
            String ip = CommonUtil.getRequestIpAddr(request);
            String token = authTokenBuilder.generateToken(entity);
            log.info("account login success, account id {}, login at {}", entity.getId(), ip);

            // setting response headers
            response.addHeader(HttpHeader.AUTHORIZATION, token);
            response.addHeader(HttpHeader.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeader.AUTHORIZATION);
            response.addHeader(HttpHeader.CACHE_CONTROL, Constant.NO_STORE);
            responseMap = ResponseMap.success(entity);
        }
        return responseMap;
    }

    @ApiOperation(value = "account logout")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.LOGOUT)
    @RequestMapping(value = "/security/logout", method = RequestMethod.GET)
    public ResponseMap<?> logout(@RequestBody RequestParameter parameters) {
        //TODO WJ
        return null;
    }

    @ApiOperation(value = "reset account password")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/resetpwd", method = RequestMethod.POST)
    public ResponseMap<?> resetPassword(@RequestBody RequestParameter parameters) {
        //TODO WJ
        return null;
    }

}
