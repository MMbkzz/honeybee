package com.stackstech.dcp.server.auth.dao;

import com.stackstech.dcp.server.auth.model.AuthOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 组织结构
 * <p>
 * <p>
 * 表名 AUTH_ORGANIZATION
 */
@Mapper
@Repository
public interface AuthOrganizationMapper {
    /**
     * 新增组织
     *
     * @param record
     * @return
     */
    int insert(AuthOrganization record);

    /**
     * 根据组织编号删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 修改组织结构
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AuthOrganization record);

    /**
     * 根据组织名称查询组织信息
     *
     * @param name 组织名称
     * @return
     */
    AuthOrganization selectByName(String name);

    /**
     * 根据父组织编号查询子组织
     *
     * @param parentId
     * @return
     */
    List<AuthOrganization> selectByParentId(Long parentId);

    /**
     * 组织结构树
     *
     * @param id
     * @return
     */
    List<AuthOrganization> getOrganizationTreeList(String id);

    /**
     * 用户 id 得到组织结构
     *
     * @param userId
     * @return
     */
    AuthOrganization selectByUserId(Long userId);

}
