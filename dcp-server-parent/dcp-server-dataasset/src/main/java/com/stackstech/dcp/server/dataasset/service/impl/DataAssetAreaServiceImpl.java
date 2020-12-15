package com.stackstech.dcp.server.dataasset.service.impl;

import com.stackstech.dcp.core.enums.ServiceStatusEnum;
import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.server.auth.dao.AuthUserMapper;
import com.stackstech.dcp.server.auth.model.AuthUser;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.dataasset.dao.DataAssetAreaMapper;
import com.stackstech.dcp.server.dataasset.dao.DataAssetTopicMapper;
import com.stackstech.dcp.server.dataasset.model.DataAssetArea;
import com.stackstech.dcp.server.dataasset.model.DataAssetTopic;
import com.stackstech.dcp.server.dataasset.service.DataAssetAreaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 数据资产领域操作业务逻辑类
 */
@Service
@Transactional
public class DataAssetAreaServiceImpl implements DataAssetAreaService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private DataAssetAreaMapper dataAssetAreaMapper;

    @Autowired
    private DataAssetTopicMapper dataAssetTopicMapper;

    @Autowired
    protected LoginUserManager loginUserManager;


    /**
     * 新增数据资产领域
     *
     * @param dataAssetArea
     * @return
     */
    @Override
    public int add(DataAssetArea dataAssetArea, HttpServletRequest req) throws Exception {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                dataAssetArea.setCreateBy(loginUser.getUserId());
                dataAssetArea.setUpdateBy(loginUser.getUserId());
            }
        }
        dataAssetArea.setId(dataAssetAreaMapper.queryPrimaryKey());
        dataAssetArea.setStatusCode(ServiceStatusEnum.enabled.toString());
        return dataAssetAreaMapper.insert(dataAssetArea);
    }

    /**
     * 资产领域详情查询
     *
     * @param dataAssetArea,page
     * @return
     */
    @Override
    public List<DataAssetArea> queryAll(DataAssetArea dataAssetArea) {
        // 获取资产领域信息
        List<DataAssetArea> areaList = null;
        if (StringUtils.isNotBlank(dataAssetArea.getQueryType())) {
            areaList = dataAssetAreaMapper.queryByCondition(dataAssetArea.getQueryString());
        } else {
            areaList = dataAssetAreaMapper.queryAll(dataAssetArea);
        }
        if (areaList != null && areaList.size() > 0) {
            for (DataAssetArea assetArea : areaList) {
                AuthUser authUser = null;
                if (assetArea.getCreateBy() != null) {
                    authUser = authUserMapper.selectByloginId(assetArea.getCreateBy());
                    assetArea.setCreateUser(authUser != null ? authUser.getLoginName() : null);
                }
                if (assetArea.getUpdateBy() != null) {
                    authUser = authUserMapper.selectByloginId(assetArea.getUpdateBy());
                    assetArea.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
                }
            }
        }
        return areaList;
    }

    /**
     * 资产领域详情查询
     *
     * @param id
     * @return
     */
    @Override
    public DataAssetArea query(String id) {
        // 获取资产领域信息
        DataAssetArea area = dataAssetAreaMapper.queryByPrimaryKey(id);
        if (area != null) {
            AuthUser authUser = null;
            if (area.getCreateBy() != null) {
                authUser = authUserMapper.selectByloginId(area.getCreateBy());
                area.setCreateUser(authUser != null ? authUser.getLoginName() : null);
            }
            if (area.getUpdateBy() != null) {
                authUser = authUserMapper.selectByloginId(area.getUpdateBy());
                area.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
            }
        }
        return area;
    }

    /**
     * 资产领域删除
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseEntity<?> delete(List<String> ids) {
        // 获取模型信息
        for (String id : ids) {
            List<DataAssetTopic> topics = dataAssetTopicMapper.queryByAreaId(id);
            if (topics != null && topics.size() > 0) {
                DataAssetTopic topic = topics.get(0);
                return ResponseError.create(500, "删除领域失败!该领域下模型" + topic.getTopicName() + "正在使用中");
            }
        }
        int i = dataAssetAreaMapper.delete(ids);
        if (i > 0) {
            return ResponseOk.create("删除领域成功");
        } else {
            return ResponseError.create(500, "删除领域失败");
        }
    }

    /**
     * 资产领域修改
     *
     * @param dataAssetArea
     * @return
     */
    @Override
    public int update(DataAssetArea dataAssetArea, HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            LoginUserProtos.LoginUser loginUser = loginUserManager.getLoginUser(token);
            if (loginUser != null) {
                dataAssetArea.setUpdateBy(loginUser.getUserId());
            }
        }
        int status = dataAssetAreaMapper.update(dataAssetArea);
        return status;
    }

    @Override
    public int countAll(DataAssetArea dataAssetArea) throws Exception {
        int i = 0;
        if (StringUtils.isNotBlank(dataAssetArea.getQueryType())) {
            i = dataAssetAreaMapper.countByCondition(dataAssetArea.getQueryString());
        } else {
            i = dataAssetAreaMapper.countAll(dataAssetArea);
        }
        return i;
    }

    @Override
    public DataAssetArea queryByName(String areaName) throws Exception {
        return dataAssetAreaMapper.queryByName(areaName);
    }
}
