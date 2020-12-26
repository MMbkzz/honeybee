package com.stackstech.honeybee.server.quality.service.impl;

import com.stackstech.honeybee.server.exception.BeesException;
import com.stackstech.honeybee.server.quality.entity.Bees;
import com.stackstech.honeybee.server.quality.entity.HoneyBees;
import com.stackstech.honeybee.server.quality.repo.BeesRepo;
import com.stackstech.honeybee.server.quality.service.BeesOrgService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stackstech.honeybee.server.exception.BeesExceptionMessage.ORGANIZATION_NAME_DOES_NOT_EXIST;

@Service
public class BeesOrgServiceImpl implements BeesOrgService {

    @Autowired
    private BeesRepo measureRepo;

    @Override
    public List<String> getOrgs() {
        return measureRepo.findOrganizations(false);
    }

    @Override
    public List<String> getMetricNameListByOrg(String org) {
        List<String> orgs = measureRepo.findNameByOrganization(org, false);
        if (CollectionUtils.isEmpty(orgs)) {
            throw new BeesException.NotFoundException
                    (ORGANIZATION_NAME_DOES_NOT_EXIST);
        }
        return orgs;
    }

    @Override
    public Map<String, List<String>> getMeasureNamesGroupByOrg() {
        Map<String, List<String>> orgWithMetricsMap = new HashMap<>();
        List<HoneyBees> measures = measureRepo.findByDeleted(false);
        for (Bees measure : measures) {
            String orgName = measure.getOrganization();
            orgName = orgName == null ? "null" : orgName;
            String measureName = measure.getName();
            List<String> measureList = orgWithMetricsMap.getOrDefault(orgName,
                    new ArrayList<>());
            measureList.add(measureName);
            orgWithMetricsMap.put(orgName, measureList);
        }
        return orgWithMetricsMap;
    }

    @Override
    public Map<String, Map<String, List<Map<String, Object>>>>
    getMeasureWithJobDetailsGroupByOrg(Map<String,
            List<Map<String, Object>>> jobDetails) {
        Map<String, Map<String, List<Map<String, Object>>>> result =
                new HashMap<>();
        List<HoneyBees> measures = measureRepo.findByDeleted(false);
        if (measures == null) {
            return null;
        }
        for (Bees measure : measures) {
            String orgName = measure.getOrganization();
            String measureName = measure.getName();
            String measureId = measure.getId().toString();
            List<Map<String, Object>> jobList = jobDetails
                    .getOrDefault(measureId, new ArrayList<>());
            Map<String, List<Map<String, Object>>> measureWithJobs = result
                    .getOrDefault(orgName, new HashMap<>());
            measureWithJobs.put(measureName, jobList);
            result.put(orgName, measureWithJobs);
        }
        return result;
    }
}
