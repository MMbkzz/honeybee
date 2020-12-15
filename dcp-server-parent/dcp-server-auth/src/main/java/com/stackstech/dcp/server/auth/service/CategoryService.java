package com.stackstech.dcp.server.auth.service;

import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.server.auth.model.AuthCategory;
import com.stackstech.dcp.server.auth.tree.OrganizationTreeBuilder;

import java.util.Map;

/**
 * 资源类别
 */
public interface CategoryService {
    /**
     * 新增资源类别
     *
     * @param record
     * @return
     */
    int insert(AuthCategory record, Long userId);

    /**
     * 删除资源类别
     *
     * @param record
     * @return
     */
    int deleteByPrimaryKey(AuthCategory record);

    /**
     * 修改资源类别
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AuthCategory record, Long userId);

    /**
     * 获取全部资源类别
     *
     * @return
     */
    Map<String, Object> getCategories(AuthCategory category, Page page);

    /**
     * 资源类别树
     *
     * @return
     */
    OrganizationTreeBuilder.Node getCategoriesTree();

}
