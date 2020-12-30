package com.stackstech.honeybee.server.quality;

import com.stackstech.honeybee.server.core.entity.*;
import com.stackstech.honeybee.server.core.enums.ApiEndpoint;
import com.stackstech.honeybee.server.core.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * DataServiceController
 *
 * @author william
 */
@RestController
@RequestMapping(value = ApiEndpoint.API_ENDPOINT_ROOT)
public class DataQualityController {

    private final Logger log = LoggerFactory.getLogger(DataQualityController.class);

    @Autowired
    private DataService<QualityJobEntity> qualityJobService;
    @Autowired
    private DataService<QualityRuleEntity> qualityRuleService;


    @RequestMapping(value = "/quality/job/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getQualityJob(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/quality/job/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteQualityJob(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/quality/job/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateQualityJob(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/quality/job/add", method = RequestMethod.PUT)
    public ResponseMap<?> addQualityJob(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/quality/job/query", method = RequestMethod.POST)
    public ResponseMap<?> queryQualityJob(@RequestBody RequestParameter parameters) {
        return null;
    }

    @RequestMapping(value = "/quality/rule/get/{id}", method = RequestMethod.GET)
    public ResponseMap<?> getQualityRule(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/quality/rule/delete/{id}", method = RequestMethod.DELETE)
    public ResponseMap<?> deleteQualityRule(@PathVariable("id") long id) {
        return null;
    }

    @RequestMapping(value = "/quality/rule/update", method = RequestMethod.PUT)
    public ResponseMap<?> updateQualityRule(@RequestBody DataServiceEntity entity) {
        return null;
    }

    @RequestMapping(value = "/quality/rule/query", method = RequestMethod.POST)
    public ResponseMap<?> queryQualityRule(@RequestBody RequestParameter parameters) {
        return null;
    }


}
