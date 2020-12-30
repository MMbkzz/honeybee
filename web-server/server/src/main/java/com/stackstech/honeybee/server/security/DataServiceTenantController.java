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
public class DataServiceTenantController {

    @RequestMapping(value = "/security/tenant/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/security/tenant/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> delete(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/security/tenant/update", method = RequestMethod.PUT)
    public ResponseMap<?> update(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/security/tenant/add", method = RequestMethod.PUT)
    public ResponseMap<?> add(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/security/tenant/query", method = RequestMethod.POST)
    public ResponseMap<?> query(@RequestBody RequestParameter parameters) {
        return null;
    }


}
