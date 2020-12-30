package com.stackstech.honeybee.server.apiserver;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * DataServiceController
 *
 * @author william
 */
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class DataServiceController {

    private final Logger log = LoggerFactory.getLogger(DataServiceController.class);

    @Autowired
    private DataService<DataServiceEntity> service;

    @RequestMapping(value = "/data/service/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        log.debug("get data service information.");
        return ResponseMap.success(service.getSingle(id));
    }

    @RequestMapping(value = "/data/service/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> delete(@PathVariable("id") long id) {
        log.debug("delete data service ID {}.", id);
        return ResponseMap.success(service.delete(id));
    }

    @RequestMapping(value = "/data/service/update", method = RequestMethod.PUT)
    public ResponseMap<?> update(@RequestBody DataServiceEntity entity) {
        boolean flag = service.update(entity);
        if (!flag) {
            return ResponseMap.failed("update data service failed.");
        }
        return ResponseMap.success(true);
    }

    @RequestMapping(value = "/data/service/add", method = RequestMethod.PUT)
    public ResponseMap<?> add(@RequestBody DataServiceEntity entity) {
        if (entity != null) {
            entity.setId(null);
        }
        boolean flag = service.add(entity);
        if (!flag) {
            return ResponseMap.failed("insert data service failed.");
        }
        return ResponseMap.success(entity);
    }

    @RequestMapping(value = "/data/service/query", method = RequestMethod.POST)
    public ResponseMap<?> query(@RequestBody RequestParameter parameters) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("pageStart", parameters.getPageStart());
        params.put("pageSize", parameters.getPageSize());
        params.put("status", parameters.getStatus());
        params.put("keywords", parameters.getKeywords());
        //TODO maybe not used ?!
        params.put("order", parameters.getOrder());

        List<DataServiceEntity> data = service.get(params);
        if (data != null && data.size() > 0) {
            int total = service.getTotalCount(params);
            log.debug("query data record size {}", total);
            return ResponseMap.setTotal(data, total);
        }
        return ResponseMap.failed("nothing found");
    }

    @RequestMapping(value = "/data/service/online/{id}", method = RequestMethod.GET)
    public ResponseMap<?> online(@PathVariable("id") long id) {
        //TODO setting data service online
        return null;
    }

    @RequestMapping(value = "/data/service/offline/{id}", method = RequestMethod.GET)
    public ResponseMap<?> offline(@PathVariable("id") long id) {
        //TODO setting data service offline
        return null;
    }

}