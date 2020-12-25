package com.stackstech.honeybee.server.auth.dao;

import com.stackstech.honeybee.server.auth.model.AuthResource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 资源
 * <p>
 * <p>
 * 表名 dgp_AUTH_RESOURCE
 */
@Mapper
@Repository
public interface AuthResourceMapper {

    int insertSelective(AuthResource record);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthResource record);

    List<AuthResource> selectListByCondition(Map<String, Object> condition);

    AuthResource selectByCondition(Map<String, Object> condition);

    /**
     * 根据 条件 查询得到 资源集合
     *
     * @param condition
     * @return
     */
    List<AuthResource> getResourcesByCondition(Map<String, Object> condition);

}
