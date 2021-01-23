package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.quality.entity.QualityJobEntity;
import com.stackstech.honeybee.server.quality.entity.QualityRuleEntity;
import com.stackstech.honeybee.server.core.vo.QualityRuleVo;
import com.stackstech.honeybee.server.quality.dao.QualityJobMapper;
import com.stackstech.honeybee.server.quality.dao.QualityRuleMapper;
import com.stackstech.honeybee.server.service.DataService;
import com.stackstech.honeybee.server.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataQualityRuleServiceImpl implements DataService<QualityRuleVo, QualityRuleEntity> {

    @Autowired
    private QualityJobMapper qualityJobMapper;
    @Autowired
    private QualityRuleMapper qualityRuleMapper;

    @Override
    public boolean add(QualityRuleVo vo, Long ownerId) {
        QualityJobEntity jobEntity = new QualityJobEntity().build(ownerId);
        BeanUtils.copyProperties(vo, jobEntity);
        jobEntity.setJobCode(CommonUtil.generateEntityCode());
        jobEntity.setJobOrder(1);
        jobEntity.setDesc(vo.getJobDesc());
        boolean flag = qualityJobMapper.insertSelective(jobEntity) > 0;
        if (flag) {
            QualityRuleEntity ruleEntity = new QualityRuleEntity().build(ownerId);
            BeanUtils.copyProperties(vo, jobEntity);
            ruleEntity.setRuleCode(CommonUtil.generateEntityCode());
            // TODO rule config yaml
            ruleEntity.setRuleConfigYaml("123");
            ruleEntity.setDesc(vo.getRuleDesc());
            ruleEntity.setJobId(jobEntity.getId());
            flag = qualityRuleMapper.insertSelective(ruleEntity) > 0;
        }
        return flag;
    }

    @Override
    public boolean update(QualityRuleVo vo, Long ownerId) {
        QualityJobEntity jobEntity = new QualityJobEntity().update(ownerId);
        BeanUtils.copyProperties(vo, jobEntity);
        jobEntity.setId(vo.getJobId());
        jobEntity.setJobCode(CommonUtil.generateEntityCode());
        jobEntity.setDesc(vo.getJobDesc());
        boolean flag = qualityJobMapper.updateByPrimaryKeySelective(jobEntity) > 0;
        if (flag) {
            QualityRuleEntity ruleEntity = new QualityRuleEntity().update(ownerId);
            BeanUtils.copyProperties(vo, jobEntity);
            ruleEntity.setId(vo.getRuleId());
            ruleEntity.setRuleCode(CommonUtil.generateEntityCode());
            // TODO rule config yaml
            ruleEntity.setRuleConfigYaml("123");
            ruleEntity.setDesc(vo.getRuleDesc());
            ruleEntity.setJobId(jobEntity.getId());
            flag = qualityRuleMapper.updateByPrimaryKeySelective(ruleEntity) > 0;
        }
        return flag;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return qualityRuleMapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public QualityRuleEntity getSingle(Long recordId) {
        return qualityRuleMapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<QualityRuleEntity> get(Map<String, Object> parameter) {
        return qualityRuleMapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return qualityRuleMapper.selectTotalCount(parameter);
    }

}
