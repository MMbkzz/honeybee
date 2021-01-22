package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.core.entity.AccountEntity;
import com.stackstech.honeybee.server.core.vo.AccountVo;
import com.stackstech.honeybee.server.dao.AccountMapper;
import com.stackstech.honeybee.server.service.DataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountServiceImpl implements DataService<AccountVo> {

    @Autowired
    private AccountMapper mapper;

    @Override
    public boolean add(AccountVo vo, Long ownerId) {
        AccountEntity entity = new AccountEntity().create(ownerId);
        BeanUtils.copyProperties(vo, entity);
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AccountVo vo, Long ownerId) {
        AccountEntity entity = new AccountEntity().update(ownerId);
        BeanUtils.copyProperties(vo, entity);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public Object getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public Object get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }
}
