package com.stackstech.honeybee.server.system.service.impl;

import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.core.service.BaseDataService;
import com.stackstech.honeybee.server.system.dao.AccountMapper;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements BaseDataService<AccountEntity> {

    @Autowired
    private AccountMapper mapper;

    @Override
    public boolean add(AccountEntity entity) throws ServerException {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AccountEntity entity) throws ServerException {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) throws ServerException {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public AccountEntity getSingle(Long recordId) throws ServerException, DataNotFoundException {
        AccountEntity entity = mapper.selectByPrimaryKey(recordId);
        CommonUtil.isNull(entity, "account not fount");
        return entity;
    }

    @Override
    public List<AccountEntity> get(Map<String, Object> parameter) throws ServerException, DataNotFoundException {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) throws ServerException {
        return mapper.selectTotalCount(parameter);
    }


}
