package com.stackstech.honeybee.server.security;

import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * authentication service controller
 *
 * @author william
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@ApiResponses(@ApiResponse(code = 404, message = "data not found", response = ResponseMap.class))
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @ApiOperation(value = "account login")
    @AuditOperation(type = AuditOperationType.SYSTEM, operation = AuditOperationType.LOGIN)
    @RequestMapping(value = "/security/login", method = RequestMethod.POST)
    public ResponseMap<?> login(@RequestBody RequestParameter parameters) {
        //TODO WJ
        return null;
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
