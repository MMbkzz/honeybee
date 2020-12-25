package com.stackstech.honeybee.server.auth.service.impl;

import com.stackstech.honeybee.core.page.IProcessPage;
import com.stackstech.honeybee.core.page.Page;
import com.stackstech.honeybee.core.page.PageUtil;
import com.stackstech.honeybee.core.util.SnowFlake;
import com.stackstech.honeybee.server.auth.dao.AuthCategoryMapper;
import com.stackstech.honeybee.server.auth.model.AuthCategory;
import com.stackstech.honeybee.server.auth.service.CategoryService;
import com.stackstech.honeybee.server.auth.tree.OrganizationTreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 资源类别操作业务逻辑类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private AuthCategoryMapper authCategoryMapper;

    @Autowired
    private SnowFlake snowflake;
    @Autowired
    private IProcessPage iProcessPage;


    @Override
    public int insert(AuthCategory record, Long userId) {
        record.setId(snowflake.next());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setCreateBy(userId);
        return authCategoryMapper.insert(record);
    }

    @Override
    public int deleteByPrimaryKey(AuthCategory record) {
        return authCategoryMapper.deleteByPrimaryKey(record.getId());
    }

    @Override
    public int updateByPrimaryKeySelective(AuthCategory record, Long userId) {
        record.setUpdateTime(new Date());
        record.setUpdateBy(userId);
        return authCategoryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Map<String, Object> getCategories(AuthCategory category, Page page) {
        if (null == page.getPageNo() || null == page.getPageSize()) {
            page.setPageNo(1);
            page.setPageSize(10);
        }
        PageUtil.page(page);
        return iProcessPage.process(authCategoryMapper.getCategories(category));
    }

    @Override
    public OrganizationTreeBuilder.Node getCategoriesTree() {
//		List<AuthCategory> categories = authCategoryMapper.getCategories();
//		CategoryTreeUtil treeUtil = new CategoryTreeUtil(categories);
        return null;
    }

}
