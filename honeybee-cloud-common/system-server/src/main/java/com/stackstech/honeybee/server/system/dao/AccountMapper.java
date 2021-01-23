package com.stackstech.honeybee.server.system.dao;

import com.stackstech.honeybee.server.system.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AccountMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AccountEntity record);

    AccountEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountEntity record);

    List<AccountEntity> selectByParameter(Map<String, Object> parameter);

    int selectTotalCount(Map<String, Object> parameter);

    AccountEntity selectByAccountAndPassowrd(Map<String, Object> parameter);
}