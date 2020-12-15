package com.stackstech.dcp.server.operation.service.impl;

import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.util.CommonUtils;
import com.stackstech.dcp.server.auth.dao.AuthUserMapper;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.operation.service.ClusterAuditService;
import com.stackstech.dcp.server.operation.vo.ClusterAuditQueryVO;
import com.stackstech.dcp.server.operations.dao.AuditLogMapper;
import com.stackstech.dcp.server.operations.model.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClusterAuditServiceImpl implements ClusterAuditService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuditLogMapper auditLogMapper;
    @Autowired
    private AuthUserMapper authUserMapper;

    @Override
    public int add(AuditLog dataClusterAudit) {
        LoginUserProtos.LoginUser loginUser = LoginUserManager.getLoginUser();
        if (loginUser != null) {
            dataClusterAudit.setCreateBy(loginUser.getUserId());
        }
        return auditLogMapper.insert(dataClusterAudit);
    }

    @Override
    public List<AuditLog> queryAll(ClusterAuditQueryVO auditQueryVO) throws Exception {
        return auditLogMapper.queryAll(CommonUtils.elementToMap(auditQueryVO));
    }

    @Override
    public int countAll(ClusterAuditQueryVO auditQueryVO) throws Exception {
        return auditLogMapper.countAll(CommonUtils.elementToMap(auditQueryVO));
    }

    @Override
    public AuditLog query(Integer id) {
        return auditLogMapper.queryByPrimaryKey(id);
    }


}
