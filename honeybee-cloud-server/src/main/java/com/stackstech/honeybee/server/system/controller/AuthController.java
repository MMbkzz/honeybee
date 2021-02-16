package com.stackstech.honeybee.server.system.controller;

import com.stackstech.honeybee.common.entity.ResponseObject;
import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.annotation.RequestAccount;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import com.stackstech.honeybee.server.system.service.AuthService;
import com.stackstech.honeybee.server.system.vo.AccountLoginVo;
import com.stackstech.honeybee.server.system.vo.RestPasswordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * authentication service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping(value = Constant.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseObject login(@Validated @RequestBody AccountLoginVo vo) {
        AccountEntity entity = authService.login(request, response, vo.getAccount(), vo.getPassword());
        return ResponseObject.build().success(entity);
    }

    @ApiOperation(value = "account logout")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.LOGOUT)
    @RequestMapping(value = "/security/logout", method = RequestMethod.GET)
    public ResponseObject logout() {
        authService.logout(request, response);
        return ResponseObject.build().success("logout success");
    }

    @ApiOperation(value = "reset account password")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.UPDATE)
    @RequestMapping(value = "/security/resetpwd", method = RequestMethod.POST)
    public ResponseObject resetPassword(@Validated @RequestBody RestPasswordVo vo, @ApiIgnore @RequestAccount AccountEntity account) {
        boolean flag = authService.resetPassword(request, response,
                vo.getAccount(),
                vo.getOldPassword(),
                vo.getNewPassword(),
                account);
        if (flag) {
            return ResponseObject.build().success("rest password success");
        }
        return ResponseObject.build().failed("rest password failed");
    }

}
