package com.stackstech.honeybee.server.auth.service.impl;

import com.stackstech.honeybee.core.page.IProcessPage;
import com.stackstech.honeybee.core.page.Page;
import com.stackstech.honeybee.core.page.PageUtil;
import com.stackstech.honeybee.core.util.SnowFlake;
import com.stackstech.honeybee.server.auth.dao.AuthOperationMapper;
import com.stackstech.honeybee.server.auth.model.AuthOperation;
import com.stackstech.honeybee.server.auth.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 操作
 */
@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private AuthOperationMapper authOperationMapper;

    @Autowired
    private SnowFlake snowflake;
    @Autowired
    private IProcessPage iProcessPage;

    @Override
    public int insert(AuthOperation record, Long UserId) {
        record.setId(snowflake.next());
        record.setCreateTime(new Date());
        record.setCreateBy(UserId);
        return authOperationMapper.insert(record);
    }

    @Override
    public int deleteByPrimaryKey(AuthOperation record) {
        return authOperationMapper.deleteByPrimaryKey(record.getId());
    }

    @Override
    public int updateByPrimaryKeySelective(AuthOperation record, Long UserId) {
        record.setUpdateTime(new Date());
        record.setUpdateBy(UserId);
        return authOperationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Map<String, Object> getOperations(AuthOperation record, Page page) {
        if (null == page.getPageNo() || null == page.getPageSize()) {
            page.setPageNo(1);
            page.setPageSize(10);
        }
        PageUtil.page(page);
        return iProcessPage.process(authOperationMapper.getOperations(record));
    }
}
