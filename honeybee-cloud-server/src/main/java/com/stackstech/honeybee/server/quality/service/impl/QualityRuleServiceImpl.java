package com.stackstech.honeybee.server.quality.service.impl;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.core.handler.MessageHandler;
import com.stackstech.honeybee.server.quality.dao.QualityJobMapper;
import com.stackstech.honeybee.server.quality.dao.QualityRuleMapper;
import com.stackstech.honeybee.server.quality.entity.QualityJobEntity;
import com.stackstech.honeybee.server.quality.entity.QualityRuleEntity;
import com.stackstech.honeybee.server.quality.service.QualityRuleService;
import com.stackstech.honeybee.server.quality.vo.QualityRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(rollbackFor = Exception.class)
@Service
public class QualityRuleServiceImpl implements QualityRuleService {

    @Autowired
    private QualityRuleMapper ruleMapper;
    @Autowired
    private QualityJobMapper jobMapper;

    private QualityJobEntity addJob(QualityRuleVo vo, Long ownerId) {
        QualityJobEntity job = new QualityJobEntity().build(ownerId).copy(vo);
        job.setDesc(vo.getJobDesc());

        if (jobMapper.insertSelective(job) > 0) {
            return job;
        }
        return null;
    }

    private boolean updateJob(QualityRuleVo vo, Long ownerId) {
        QualityJobEntity job = new QualityJobEntity().update(ownerId).copy(vo);
        job.setId(vo.getJobId());
        job.setDesc(vo.getJobDesc());

        return jobMapper.updateByPrimaryKeySelective(job) > 0;
    }

    @Override
    public boolean add(QualityRuleEntity entity) throws ServerException {
        QualityRuleVo ruleVo = entity.getQualityRuleVo();
        QualityJobEntity job = addJob(ruleVo, entity.getOwner());
        if (job == null) {
            return false;
        }
        entity.setJobId(job.getId());
        entity.setDesc(ruleVo.getRuleDesc());
        List<String> expressions = ruleVo.getRuleExpressions();
        if (expressions == null || expressions.isEmpty()) {
            throw new ServerException(MessageHandler.of().message("quality.rule.empty"));
        }
        JsonParameterList parameterList = new JsonParameterList();
        parameterList.addAll(expressions);
        entity.setRuleExpression(parameterList);
        // TODO rule config yaml
        entity.setRuleConfigYaml("123");
        return ruleMapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(QualityRuleEntity entity) throws ServerException {
        QualityRuleVo ruleVo = entity.getQualityRuleVo();
        if (!updateJob(ruleVo, entity.getOwner())) {
            return false;
        }
        entity.setId(ruleVo.getRuleId());
        entity.setJobId(ruleVo.getJobId());
        entity.setDesc(ruleVo.getRuleDesc());
        List<String> expressions = ruleVo.getRuleExpressions();
        if (expressions != null && expressions.size() > 0) {
            JsonParameterList parameterList = new JsonParameterList();
            parameterList.addAll(expressions);
            entity.setRuleExpression(parameterList);
            // TODO rule config yaml
            entity.setRuleConfigYaml("123");
        }
        return ruleMapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) throws ServerException {
        return ruleMapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public QualityRuleEntity getSingle(Long recordId) throws ServerException, DataNotFoundException {
        QualityRuleEntity rule = ruleMapper.selectByPrimaryKey(recordId);
        CommonUtil.isNull(rule, MessageHandler.of().message("data.not.found"));
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
        return ruleMapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) throws ServerException {
        return ruleMapper.selectTotalCount(parameter);
    }

}
