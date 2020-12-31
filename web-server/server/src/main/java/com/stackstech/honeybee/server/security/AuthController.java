package com.stackstech.honeybee.server.security;

import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * authentication service controller
 *
 * @author william
 */
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class AuthController {

    @RequestMapping(value = "/security/login", method = RequestMethod.POST)
    public ResponseMap<?> login(@RequestBody RequestParameter parameters) {
        //TODO WJ
        return null;
    }

    @RequestMapping(value = "/security/logout", method = RequestMethod.GET)
    public ResponseMap<?> logout(@RequestBody RequestParameter parameters) {
        //TODO WJ
        return null;
    }

    @RequestMapping(value = "/security/resetpwd", method = RequestMethod.POST)
    public ResponseMap<?> resetPassword(@RequestBody RequestParameter parameters) {
        //TODO WJ
        return null;
    }

}
