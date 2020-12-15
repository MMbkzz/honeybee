package com.stackstech.dcp.server.auth.service.impl;

import com.stackstech.dcp.core.page.IProcessPage;
import com.stackstech.dcp.core.page.Page;
import com.stackstech.dcp.core.page.PageUtil;
import com.stackstech.dcp.core.util.SnowFlake;
import com.stackstech.dcp.server.auth.dao.AuthOperationMapper;
import com.stackstech.dcp.server.auth.model.AuthOperation;
import com.stackstech.dcp.server.auth.service.OperationService;
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
