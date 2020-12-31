package com.stackstech.honeybee.server.security;

import com.stackstech.honeybee.server.core.entity.AccountEntity;
import com.stackstech.honeybee.server.core.mapper.AccountMapper;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements DataService<AccountEntity> {

    @Autowired
    private AccountMapper mapper;

    @Override
    public boolean add(AccountEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AccountEntity entity) {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public AccountEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public AccountEntity getSingle(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public List<AccountEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }
}
