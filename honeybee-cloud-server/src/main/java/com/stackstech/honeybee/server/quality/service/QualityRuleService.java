package com.stackstech.honeybee.server.quality.service;

import com.stackstech.honeybee.server.quality.vo.QualityRuleVo;
import com.stackstech.honeybee.server.quality.entity.QualityRuleEntity;

import java.util.List;
import java.util.Map;

public interface QualityRuleService {

    boolean add(QualityRuleVo vo, Long ownerId);

    boolean update(QualityRuleVo vo, Long ownerId);

    boolean delete(Long recordId, Long ownerId);

    QualityRuleEntity getSingle(Long recordId);

    List<QualityRuleEntity> get(Map<String, Object> parameter);

    Integer getTotalCount(Map<String, Object> parameter);
}
