package com.stackstech.honeybee.server.quality.service.impl;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.quality.dao.QualityJobMapper;
import com.stackstech.honeybee.server.quality.dao.QualityRuleMapper;
import com.stackstech.honeybee.server.quality.entity.QualityJobEntity;
import com.stackstech.honeybee.server.quality.entity.QualityRuleEntity;
import com.stackstech.honeybee.server.quality.service.QualityRuleService;
import com.stackstech.honeybee.server.quality.vo.QualityRuleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QualityRuleServiceImpl implements QualityRuleService {

    @Autowired
    private QualityRuleMapper ruleMapper;
    @Autowired
    private QualityJobMapper jobMapper;

    private QualityJobEntity addJob(QualityRuleVo vo, Long ownerId) {
        QualityJobEntity job = new QualityJobEntity().build(ownerId);
        BeanUtils.copyProperties(vo, job);
        job.setDesc(vo.getJobDesc());

        if (jobMapper.insertSelective(job) > 0) {
            return job;
        }
        return null;
    }

    private boolean updateJob(QualityRuleVo vo, Long ownerId) {
        QualityJobEntity job = new QualityJobEntity().update(ownerId);
        BeanUtils.copyProperties(vo, job);
        job.setId(vo.getJobId());
        job.setDesc(vo.getJobDesc());

        return jobMapper.updateByPrimaryKeySelective(job) > 0;
    }

    @Override
    public boolean add(QualityRuleVo vo, Long ownerId) throws ServerException {
        QualityJobEntity job = addJob(vo, ownerId);
        if (job == null) {
            return false;
        }

        QualityRuleEntity rule = new QualityRuleEntity().build(ownerId);
        BeanUtils.copyProperties(vo, rule);
        // TODO rule config yaml
        rule.setRuleConfigYaml("123");
        rule.setDesc(vo.getRuleDesc());
        rule.setJobId(job.getId());

        return ruleMapper.insertSelective(rule) > 0;
    }

    @Override
    public boolean update(QualityRuleVo vo, Long ownerId) throws ServerException {
        if (!updateJob(vo, ownerId)) {
            return false;
        }
        QualityRuleEntity rule = new QualityRuleEntity().update(ownerId);
        BeanUtils.copyProperties(vo, rule);
        rule.setId(vo.getRuleId());
        // TODO rule config yaml
        rule.setRuleConfigYaml("123");
        rule.setDesc(vo.getRuleDesc());
        rule.setJobId(vo.getJobId());
        return ruleMapper.updateByPrimaryKeySelective(rule) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) throws ServerException {
        return ruleMapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public QualityRuleEntity getSingle(Long recordId) throws ServerException, DataNotFoundException {
        QualityRuleEntity rule = ruleMapper.selectByPrimaryKey(recordId);
        CommonUtil.isNull(rule, "quality rule not found");
        QualityJobEntity job = jobMapper.selectByPrimaryKey(rule.getJobId());
        Map<String, Object> maps = Maps.newLinkedHashMap();
        maps.put("jobName", job.getJobName());
        maps.put("jobCode", job.getJobCode());
        maps.put("jobExpression", job.getJobExpression());
        maps.put("jobOrder", job.getJobOrder());
        maps.put("jobDesc", job.getDesc());
        rule.setJob(maps);
        return rule;
    }

    @Override
    public List<QualityRuleEntity> get(Map<String, Object> parameter) throws ServerException, DataNotFoundException {
        List<QualityRuleEntity> entities = ruleMapper.selectByParameter(parameter);
        CommonUtil.isEmpty(entities);
        return entities;
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) throws ServerException {
        return ruleMapper.selectTotalCount(parameter);
    }
}
