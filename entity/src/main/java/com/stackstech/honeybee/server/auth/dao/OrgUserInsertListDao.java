package com.stackstech.honeybee.server.auth.dao;

import com.stackstech.honeybee.core.util.MyBatisBatchSupport;
import com.stackstech.honeybee.server.auth.model.AuthOrgUser;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrgUserInsertListDao {

    @Resource(name = "sqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    public void insertList(List<AuthOrgUser> orgUsers) {
        MyBatisBatchSupport.batchInsertByMapper(sqlSessionFactory, AuthOrgUserMapper.class, AuthOrgUser.class, "insert", orgUsers);
    }

}
