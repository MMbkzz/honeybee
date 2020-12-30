package com.stackstech.honeybee.server.security;

import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import org.springframework.web.bind.annotation.*;

/**
 * DataServiceController
 *
 * @author william
 */
//TODO GYF
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class AccountController {

    @RequestMapping(value = "/security/account/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getAccount(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/security/account/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteAccount(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/security/account/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateAccount(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/security/account/add", method = RequestMethod.PUT)
    public ResponseMap<?> addAccount(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/security/account/query", method = RequestMethod.POST)
    public ResponseMap<?> queryAccount(@RequestBody RequestParameter parameters) {
        return null;
    }

    @RequestMapping(value = "/security/role/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getAcctRole(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/security/role/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteAcctRole(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/security/role/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateAcctRole(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/security/role/add", method = RequestMethod.PUT)
    public ResponseMap<?> addAcctRole(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/security/role/query", method = RequestMethod.POST)
    public ResponseMap<?> queryAcctRole(@RequestBody RequestParameter parameters) {
        return null;
    }


}
