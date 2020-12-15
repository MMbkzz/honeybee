package com.stackstech.dcp.server.auth.dao;

import com.stackstech.dcp.core.util.MyBatisBatchSupport;
import com.stackstech.dcp.server.auth.model.AuthUserRole;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserRoleInsertListDao {

    @Resource(name = "sqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    public void insertList(List<AuthUserRole> userRoles) {
        MyBatisBatchSupport.batchInsertByMapper(sqlSessionFactory, AuthUserRoleMapper.class, AuthUserRole.class, "insert", userRoles);
    }

}
