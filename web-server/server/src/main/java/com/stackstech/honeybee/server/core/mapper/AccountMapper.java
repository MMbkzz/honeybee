package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.AccountEntity;

public interface AccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AccountEntity record);

    int insertSelective(AccountEntity record);

    AccountEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountEntity record);

    int updateByPrimaryKey(AccountEntity record);
}