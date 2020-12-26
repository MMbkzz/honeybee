package com.stackstech.honeybee.server.quality.controller;

import com.stackstech.honeybee.server.quality.service.BeesOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
public class BeesOrgController {

    @Autowired
    private BeesOrgService beesOrgService;

    @RequestMapping(value = "/org", method = RequestMethod.GET)
    public List<String> getOrgs() {
        return beesOrgService.getOrgs();
    }

    /**
     * @param org organization name
     * @return list of metric name, and a metric is the result of executing the
     * job sharing the same name with measure.
     */
    @RequestMapping(value = "/org/{org}", method = RequestMethod.GET)
    public List<String> getMetricNameListByOrg(@PathVariable("org") String org) {
        return beesOrgService.getMetricNameListByOrg(org);
    }

    @RequestMapping(value = "/org/measure/names", method = RequestMethod.GET)
    public Map<String, List<String>> getMeasureNamesGroupByOrg() {
        return beesOrgService.getMeasureNamesGroupByOrg();
    }
}
