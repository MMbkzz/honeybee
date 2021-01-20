package com.stackstech.honeybee.server.security;

import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.entity.AccountEntity;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.vo.AccountLoginVo;
import com.stackstech.honeybee.server.core.vo.RestPasswordVo;
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
import javax.validation.Valid;

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
    private AuthService authService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @ApiOperation(value = "account login")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.LOGIN)
    @RequestMapping(value = "/security/login", method = RequestMethod.POST)
    public ResponseMap<?> login(@Valid @RequestBody AccountLoginVo parameters) {
        ResponseMap responseMap = ResponseMap.failed("login failed, please check your account and password");

        AccountEntity entity = authService.login(request, response,
                parameters.getAccount(), parameters.getPassword());
        if (entity != null) {
            responseMap = ResponseMap.success(entity);
        }
        return responseMap;
    }

    @ApiOperation(value = "account logout")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.LOGOUT)
    @RequestMapping(value = "/security/logout", method = RequestMethod.GET)
    public ResponseMap<?> logout() {
        authService.logout(request, response);
        return ResponseMap.success("logout success");
    }

    @ApiOperation(value = "reset account password")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/resetpwd", method = RequestMethod.POST)
    public ResponseMap<?> resetPassword(@Valid @RequestBody RestPasswordVo parameters) {

        boolean flag = authService.resetPassword(request, response,
                parameters.getAccount(),
                parameters.getOldPassword(),
                parameters.getNewPassword());
        if (flag) {
            return ResponseMap.success("rest password success");
        }
        return ResponseMap.failed("rest password failed");
    }

}
