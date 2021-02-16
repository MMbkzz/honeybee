package com.stackstech.honeybee.server.quality.service;

import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.quality.entity.QualityRuleEntity;
import com.stackstech.honeybee.server.quality.vo.QualityRuleVo;

import java.util.List;
import java.util.Map;

public interface QualityRuleService {

    boolean add(QualityRuleVo vo, Long ownerId) throws ServerException;

    boolean update(QualityRuleVo vo, Long ownerId) throws ServerException;

    boolean delete(Long recordId, Long ownerId) throws ServerException;

    QualityRuleEntity getSingle(Long recordId) throws ServerException, DataNotFoundException;

    List<QualityRuleEntity> get(Map<String, Object> parameter) throws ServerException, DataNotFoundException;

    Integer getTotalCount(Map<String, Object> parameter) throws ServerException;
}
