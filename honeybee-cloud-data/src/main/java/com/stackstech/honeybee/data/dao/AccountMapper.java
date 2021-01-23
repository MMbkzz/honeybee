package com.stackstech.honeybee.data.dao;

import com.stackstech.honeybee.data.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface AccountMapper {
    AccountEntity selectByAccountAndPassowrd(Map<String, Object> parameter);
}