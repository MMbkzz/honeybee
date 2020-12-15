package com.stackstech.honeybee.server.operation.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stackstech.honeybee.core.util.CommonUtils;
import com.stackstech.honeybee.server.auth.dao.AuthUserMapper;
import com.stackstech.honeybee.server.auth.model.AuthUser;
import com.stackstech.honeybee.server.operation.service.SysAuditLogService;
import com.stackstech.honeybee.server.operation.vo.SysAuditQueryVO;
import com.stackstech.honeybee.server.operations.dao.SysAuditLogMapper;
import com.stackstech.honeybee.server.operations.model.SysAuditLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysAuditLogServiceImpl implements SysAuditLogService {

    @Autowired
    private SysAuditLogMapper sysAuditLogMapper;
    @Autowired
    private AuthUserMapper authUserMapper;

    @Override
    public int add(SysAuditLog sysAuditLog) throws Exception {
        return sysAuditLogMapper.insert(sysAuditLog);
    }

    /**
     * 查询所有平台日志列表
     *
     * @param sysAuditQueryVO
     * @return
     */
    @Override
    public Map<String, Object> queryAll(SysAuditQueryVO sysAuditQueryVO) throws Exception {
        PageInfo<SysAuditLog> pageInfo = new PageInfo<>();
        if (StringUtils.isNotEmpty(String.valueOf(sysAuditQueryVO.getPageNo())) && StringUtils.isNotEmpty(String.valueOf(sysAuditQueryVO.getPageSize()))) {
            Page<SysAuditLog> objects = PageHelper.startPage(Integer.parseInt(String.valueOf(sysAuditQueryVO.getPageNo())), Integer.parseInt(String.valueOf(sysAuditQueryVO.getPageSize())));
            sysAuditLogMapper.queryAll(CommonUtils.elementToMap(sysAuditQueryVO));
            pageInfo = new PageInfo<>(objects);
        } else {
            List<SysAuditLog> sysAuditLogs = sysAuditLogMapper.queryAll(CommonUtils.elementToMap(sysAuditQueryVO));
            pageInfo = new PageInfo<>(sysAuditLogs);
        }
        List<SysAuditLog> sysAuditLogs = pageInfo.getList();
        if (sysAuditLogs != null && sysAuditLogs.size() > 0) {
            for (SysAuditLog auditLog : sysAuditLogs) {
                AuthUser authUser = authUserMapper.selectByloginId(auditLog.getUserId());
                if (authUser != null) {
                    auditLog.setUserName(authUser.getLoginName());
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("count", Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        map.put("list", pageInfo.getList());
        return map;
    }

    /**
     * 查询平台日志
     *
     * @param id
     * @return
     */
    @Override
    public SysAuditLog query(Long id) throws Exception {
        // 获取平台信息
        SysAuditLog area = sysAuditLogMapper.queryByPrimaryKey(id);
        return area;
    }

    /**
     * 获取平台日志记录
     *
     * @param sysAuditQueryVO
     * @return
     * @throws Exception
     */
    @Override
    public int countAll(SysAuditQueryVO sysAuditQueryVO) throws Exception {
        return sysAuditLogMapper.countAll(CommonUtils.elementToMap(sysAuditQueryVO));
    }

}
