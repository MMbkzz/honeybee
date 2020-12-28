package com.stackstech.honeybee.server.apiserver.controller;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.apiserver.service.DataService;
import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.entity.RequestParameter;
import com.stackstech.honeybee.server.core.entity.ResponseMap;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
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
//TODO WJ
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class DataServiceController {

    private final Logger log = LoggerFactory.getLogger(DataServiceController.class);

    @Autowired
    private DataService dataService;

    @RequestMapping(value = "/data/service/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> get(@PathVariable("id") long id) {
        log.debug("get data service information.");
        return ResponseMap.success(dataService.getDataService(id));
    }

    @RequestMapping(value = "/data/service/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> delete(@PathVariable("id") long id) {
        log.debug("delete data service ID {}.", id);
        return ResponseMap.success(dataService.deleteDataService(id));
    }

    @RequestMapping(value = "/data/service/update", method = RequestMethod.PUT)
    public ResponseMap<?> update(@RequestBody DataServiceEntity entity) {
        if (entity != null) {
            entity.setId(-1L);
        }
        boolean flag = dataService.updateDataService(entity);
        if (!flag) {
            return ResponseMap.failed("update data service failed.");
        }
        return ResponseMap.success(entity);
    }

    @RequestMapping(value = "/data/service/add", method = RequestMethod.PUT)
    public ResponseMap<?> add(@RequestBody DataServiceEntity entity) {
        boolean flag = dataService.updateDataService(entity);
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
        params.put("order", parameters.getOrder());
        params.put("keywords", parameters.getKeywords());

        List<DataServiceEntity> data = dataService.getDataServices(params);
        if (data != null && data.size() > 0) {
            int total = dataService.getDataServiceCount(params);
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
