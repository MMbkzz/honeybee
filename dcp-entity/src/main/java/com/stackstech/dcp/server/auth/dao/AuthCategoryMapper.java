package com.stackstech.dcp.server.auth.dao;

import com.stackstech.dcp.server.auth.model.AuthCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资源类别
 * <p>
 * <p>
 * 表名 AUTH_CATEGORY
 */
@Mapper
@Repository
public interface AuthCategoryMapper {
    /**
     * 新增资源类别
     *
     * @param record
     * @return
     */
    int insert(AuthCategory record);

    int insertSelective(AuthCategory record);

    /**
     * 删除资源类别
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 修改资源类别
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AuthCategory record);

    /**
     * 获取全部资源类别
     *
     * @return
     */
    List<AuthCategory> getCategories(AuthCategory category);

}
