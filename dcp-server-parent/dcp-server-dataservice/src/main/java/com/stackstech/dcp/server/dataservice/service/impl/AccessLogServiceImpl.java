package com.stackstech.dcp.server.dataservice.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.core.util.CommonUtils;
import com.stackstech.dcp.server.auth.dao.AuthUserMapper;
import com.stackstech.dcp.server.auth.model.AuthUser;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.dataservice.dao.AccessLogMapper;
import com.stackstech.dcp.server.dataservice.dao.AppUserMapper;
import com.stackstech.dcp.server.dataservice.dao.DataServiceMapper;
import com.stackstech.dcp.server.dataservice.model.AccessLog;
import com.stackstech.dcp.server.dataservice.model.AppUser;
import com.stackstech.dcp.server.dataservice.model.DataService;
import com.stackstech.dcp.server.dataservice.service.AccessLogService;
import com.stackstech.dcp.server.dataservice.vo.AccessLogQueryVO;
import com.stackstech.dcp.server.dataservice.vo.AccessLogVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 数据服务API访问日志业务逻辑类
 */
@Service
@Transactional
public class AccessLogServiceImpl implements AccessLogService {

    private static final Logger log = LoggerFactory.getLogger(AccessLogServiceImpl.class);

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private DataServiceMapper dataServiceMapper;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private AuthUserMapper authUserMapper;

    /**
     * 新增数据服务API访问日志
     *
     * @param accessLog
     * @return
     */
    @Override
    public int add(AccessLog accessLog) {
        LoginUserProtos.LoginUser loginUser = LoginUserManager.getLoginUser();
        if (loginUser != null) {
            accessLog.setCreateBy(loginUser.getUserId());
        }
        int i = accessLogMapper.insert(accessLog);
        return i;
    }

    /**
     * 数据服务API访问日志详情查询
     *
     * @param accessLogQueryVO
     * @return
     */
    @Override
    public Map<String, Object> queryAll(AccessLogQueryVO accessLogQueryVO) throws Exception {
        // 获取数据服务API访问日志信息
        PageInfo<AccessLog> pageInfo = new PageInfo<>();
        if (StringUtils.isNotEmpty(String.valueOf(accessLogQueryVO.getPageNo())) && StringUtils.isNotEmpty(String.valueOf(accessLogQueryVO.getPageSize()))) {
            Page<AccessLog> objects = PageHelper.startPage(Integer.parseInt(String.valueOf(accessLogQueryVO.getPageNo())), Integer.parseInt(String.valueOf(accessLogQueryVO.getPageSize())));
            if (StringUtils.isNotBlank(accessLogQueryVO.getQueryType())) {
                accessLogMapper.queryByCondition(accessLogQueryVO.getQueryString());
            } else {
                accessLogMapper.queryAll(CommonUtils.elementToMap(accessLogQueryVO));
            }
            pageInfo = new PageInfo<>(objects);
        } else {
            List<AccessLog> accessLogs = null;
            if (StringUtils.isNotBlank(accessLogQueryVO.getQueryType())) {
                accessLogs = accessLogMapper.queryByCondition(accessLogQueryVO.getQueryString());
            } else {
                accessLogs = accessLogMapper.queryAll(CommonUtils.elementToMap(accessLogQueryVO));
            }
            pageInfo = new PageInfo<>(accessLogs);
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("count", Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        map.put("list", pageInfo.getList());
        return map;
    }

    /**
     * 数据服务API访问日志详情查询
     *
     * @param id
     * @return
     */
    @Override
    public AccessLogVO query(Integer id) {
        // 获取数据服务API访问日志信息
        AccessLog accessLog = accessLogMapper.queryByPrimaryKey(id);
        AuthUser authUser = null;
        if (accessLog.getCreateBy() != null) {
            authUser = authUserMapper.selectByloginId(accessLog.getCreateBy());
            accessLog.setCreateUser(authUser != null ? authUser.getLoginName() : null);
        }
        if (accessLog.getUpdateBy() != null) {
            authUser = authUserMapper.selectByloginId(accessLog.getUpdateBy());
            accessLog.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
        }
        DataService dataService = dataServiceMapper.queryByPrimaryKey(accessLog.getDataServiceId());
        AppUser appUser = appUserMapper.queryByPrimaryKey(accessLog.getAppId());
        AccessLogVO accessLogVO = this.parseLog2LogVO(accessLog, dataService, appUser);
        return accessLogVO;
    }

    /**
     * 数据服务API访问日志详情查询
     *
     * @param id
     * @return
     */
    @Override
    public int delete(Integer id) {
        // 获取数据服务API访问日志信息
        int i = accessLogMapper.delete(id);
        return i;
    }

    /**
     * 数据服务API访问日志修改
     *
     * @param accessLog
     * @return
     */
    @Override
    public int update(AccessLog accessLog) {
        // 获取数据服务API访问日志信息
        LoginUserProtos.LoginUser loginUser = LoginUserManager.getLoginUser();
        if (loginUser != null) {
            accessLog.setUpdateBy(loginUser.getUserId());
        }
        int status = accessLogMapper.update(accessLog);
        return status;
    }

    @Override
    public int countAll(AccessLogQueryVO accessLogQueryVO) throws Exception {
        // 获取数据服务API访问日志信息
        int i = 0;
        if (StringUtils.isNotBlank(accessLogQueryVO.getQueryType())) {
            i = accessLogMapper.countByCondition(accessLogQueryVO.getQueryString());
        } else {
            i = accessLogMapper.countAll(CommonUtils.elementToMap(accessLogQueryVO));
        }
        return i;
    }

    /**
     * 将AccessLog转化为AccessLogVO
     *
     * @param accessLog
     * @param dataService
     * @param appUser
     * @return
     */
    private AccessLogVO parseLog2LogVO(AccessLog accessLog, DataService dataService, AppUser appUser) {
        if (accessLog != null) {
            AccessLogVO accessLogVO = new AccessLogVO();
            accessLogVO.setId(accessLog.getId());
            accessLogVO.setAppId(accessLog.getAppId());
            accessLogVO.setDataServiceId(accessLog.getDataServiceId());
            accessLogVO.setInstanceHost(accessLog.getInstanceHost());
            accessLogVO.setInstancePort(accessLog.getInstancePort());
            accessLogVO.setMessage(accessLog.getMessage());
            accessLogVO.setCreateTime(accessLog.getCreateTime());
            accessLogVO.setCreateUser(accessLog.getCreateUser());
            accessLogVO.setUpdateUser(accessLog.getUpdateUser());
            accessLogVO.setRequestParams(accessLog.getRequestParams());
            accessLogVO.setAccessStartTime(accessLog.getAccessStartTime());
            accessLogVO.setAccessEndTime(accessLog.getAccessEndTime());
            accessLogVO.setDbStartTime(accessLog.getDbStartTime());
            accessLogVO.setDbEndTime(accessLog.getDbEndTime());
            accessLogVO.setClientHost(accessLog.getClientHost());
            accessLogVO.setAccessTimes(accessLog.getAccessTimes());
            accessLogVO.setExecTimes(accessLog.getExecTimes());

            if (dataService != null) {
                accessLogVO.setDataServiceName(dataService.getDataServiceName());
            }
            if (appUser != null) {
                accessLogVO.setName(appUser.getName());
            }
            return accessLogVO;
        }
        return null;
    }
}
