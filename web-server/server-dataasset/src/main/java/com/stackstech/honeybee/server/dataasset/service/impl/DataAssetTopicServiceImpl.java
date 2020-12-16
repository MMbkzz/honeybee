package com.stackstech.honeybee.server.dataasset.service.impl;

import com.stackstech.honeybee.core.enums.ServiceStatusEnum;
import com.stackstech.honeybee.core.http.ResponseError;
import com.stackstech.honeybee.core.http.ResponseOk;
import com.stackstech.honeybee.core.model.LoginUserProtos;
import com.stackstech.honeybee.core.util.CommonUtils;
import com.stackstech.honeybee.core.util.DateUtils;
import com.stackstech.honeybee.server.auth.dao.AuthUserMapper;
import com.stackstech.honeybee.server.auth.model.AuthUser;
import com.stackstech.honeybee.server.auth.utils.LoginUserManager;
import com.stackstech.honeybee.server.auth.utils.TokenUtil;
import com.stackstech.honeybee.server.dataasset.dao.DataAssetAreaMapper;
import com.stackstech.honeybee.server.dataasset.dao.DataAssetTopicMapper;
import com.stackstech.honeybee.server.dataasset.dao.ServiceModelMapper;
import com.stackstech.honeybee.server.dataasset.model.DataAssetArea;
import com.stackstech.honeybee.server.dataasset.model.DataAssetTopic;
import com.stackstech.honeybee.server.dataasset.model.ServiceModel;
import com.stackstech.honeybee.server.dataasset.service.DataAssetTopicService;
import com.stackstech.honeybee.server.dataasset.vo.DataAssetTopicQueryVO;
import com.stackstech.honeybee.server.dataasset.vo.DataAssetTopicVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据资产主题操作业务逻辑类
 */
@Service
@Transactional
public class DataAssetTopicServiceImpl implements DataAssetTopicService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataAssetTopicMapper dataAssetTopicMapper;

    @Autowired
    private DataAssetAreaMapper dataAssetAreaMapper;

    @Autowired
    private ServiceModelMapper serviceModelMapper;

    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    protected LoginUserManager loginUserManager;

    /**
     * 新增数据资产主题
     *
     * @param dataAssetTopic
     * @return
     */
    @Override
    public int add(DataAssetTopic dataAssetTopic, Long userId) {
        dataAssetTopic.setCreateBy(userId);
        dataAssetTopic.setCreateTime(DateUtils.getDefaultCurrentTimeStamp(new Date()));
        dataAssetTopic.setId(dataAssetTopicMapper.queryPrimaryKey());
        dataAssetTopic.setStatusCode(ServiceStatusEnum.enabled.toString());
        return dataAssetTopicMapper.insert(dataAssetTopic);
    }

    /**
     * 资产主题详情查询
     *
     * @param queryVO
     * @return
     */
    @Override
    public List<Map<String, Object>> queryAll(DataAssetTopicQueryVO queryVO) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 获取资产主题信息
        List<DataAssetTopic> topicList = null;
        if (StringUtils.isNotBlank(queryVO.getQueryType())) {
            topicList = dataAssetTopicMapper.queryByCondition(queryVO.getQueryString(), queryVO.getUserId());
        } else {
            topicList = dataAssetTopicMapper.queryAll(CommonUtils.elementToMap(queryVO));
        }
        if (topicList != null && topicList.size() > 0) {
            for (DataAssetTopic dataAssetTopic : topicList) {
                //获取更新用户
                this.queryUpdateUser(dataAssetTopic);

                Map<String, Object> topicMap = CommonUtils.elementToMap(dataAssetTopic);
                DataAssetArea queryArea = dataAssetAreaMapper.queryByPrimaryKey(dataAssetTopic.getAreaId());
                if (queryArea != null) {
                    topicMap.put("areaName", queryArea.getAreaName());
                }
                list.add(topicMap);
            }
        }
        return list;
    }

    /**
     * 资产主题详情查询
     *
     * @param id
     * @return
     */
    @Override
    public DataAssetTopicVO query(String id) {
        // 获取资产主题信息
        DataAssetTopic topic = dataAssetTopicMapper.queryByPrimaryKey(id);
        if (topic != null) {
            //获取更新用户
            this.queryUpdateUser(topic);
            DataAssetTopicVO assetTopicVO = parseTopic2AssetTopicVO(topic, dataAssetAreaMapper.queryByPrimaryKey(topic.getAreaId()));
            return assetTopicVO;
        }
        return null;
    }

    /**
     * 资产主题详情查询
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseEntity<?> delete(List<String> ids) {
        // 获取模型信息
        for (String topicId : ids) {
            List<ServiceModel> serviceModels = serviceModelMapper.queryByTopicId(topicId);
            if (serviceModels != null && serviceModels.size() > 0) {
                ServiceModel model = serviceModels.get(0);
                return ResponseError.create(500, "删除主题失败!该主题下模型" + model.getModelName() + "正在使用中");
            }
        }
        int i = dataAssetTopicMapper.delete(ids);
        if (i > 0) {
            return ResponseOk.create("删除主题成功");
        } else {
            return ResponseError.create(500, "删除主题失败");
        }
    }

    /**
     * 资产主题修改
     *
     * @param dataAssetTopic
     * @return
     */
    @Override
    public int update(DataAssetTopic dataAssetTopic, Long userId) {
        dataAssetTopic.setUpdateBy(userId);
        dataAssetTopic.setUpdateTime(DateUtils.getDefaultCurrentTimeStamp(new Date()));
        return dataAssetTopicMapper.update(dataAssetTopic);

    }

    @Override
    public int countAll(DataAssetTopicQueryVO queryVO) throws Exception {
        int i = 0;
        if (StringUtils.isNotBlank(queryVO.getQueryType())) {
            i = dataAssetTopicMapper.countByCondition(queryVO.getQueryString(), queryVO.getUserId());
        } else {
            i = dataAssetTopicMapper.countAll(CommonUtils.elementToMap(queryVO));
        }
        return i;
    }

    @Override
    public DataAssetTopic queryByName(String topicName) {
        return dataAssetTopicMapper.queryByName(topicName);
    }

    /**
     * 查询更新用户
     *
     * @param topic
     */
    private void queryUpdateUser(DataAssetTopic topic) {
        AuthUser authUser = null;
        if (topic.getCreateBy() != null) {
            authUser = authUserMapper.selectByloginId(topic.getCreateBy());
            topic.setCreateUser(authUser != null ? authUser.getLoginName() : null);
        }
        if (topic.getUpdateBy() != null) {
            authUser = authUserMapper.selectByloginId(topic.getUpdateBy());
            topic.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
        }
    }

    /**
     * 返回LoginUser
     *
     * @param req
     * @return
     */
    private LoginUserProtos.LoginUser getLoginUser(HttpServletRequest req) {
        String token = TokenUtil.getToken(req);
        if (token != null) {
            return loginUserManager.getLoginUser(token);
        }
        return null;
    }

    /**
     * 将DataAssetTopic转化成DataAssetTopicVO
     *
     * @param dataAssetTopic
     * @return
     */
    private DataAssetTopicVO parseTopic2AssetTopicVO(DataAssetTopic dataAssetTopic, DataAssetArea dataAssetArea) {
        if (dataAssetTopic != null) {
            DataAssetTopicVO dataAssetTopicVO = new DataAssetTopicVO();
            dataAssetTopicVO.setId(dataAssetTopic.getId());
            dataAssetTopicVO.setAreaId(dataAssetTopic.getAreaId());
            dataAssetTopicVO.setTopicName(dataAssetTopic.getTopicName());
            dataAssetTopicVO.setTopicDesc(dataAssetTopic.getTopicDesc());
            dataAssetTopicVO.setStatusCode(dataAssetTopic.getStatusCode());
            dataAssetTopicVO.setCreateBy(dataAssetTopic.getCreateBy());
            dataAssetTopicVO.setCreateTime(dataAssetTopic.getCreateTime());
            dataAssetTopicVO.setUpdateBy(dataAssetTopic.getUpdateBy());
            dataAssetTopicVO.setUpdateTime(dataAssetTopic.getUpdateTime());
            dataAssetTopicVO.setCreateUser(dataAssetTopic.getCreateUser());
            dataAssetTopicVO.setUpdateUser(dataAssetTopic.getUpdateUser());

            dataAssetTopicVO.setDataAssetArea(dataAssetArea);
            return dataAssetTopicVO;
        }
        return null;
    }

    /**
     * 将DataAssetTopicVO转化成DataAssetTopic
     *
     * @param dataAssetTopicVO
     * @return
     */
    private DataAssetTopic parseAssetTopicVO2Topic(DataAssetTopicVO dataAssetTopicVO) {
        if (dataAssetTopicVO != null) {
            DataAssetTopic dataAssetTopic = new DataAssetTopic();
            dataAssetTopic.setId(dataAssetTopicVO.getId());
            dataAssetTopic.setAreaId(dataAssetTopicVO.getAreaId());
            dataAssetTopic.setTopicName(dataAssetTopicVO.getTopicName());
            dataAssetTopic.setTopicDesc(dataAssetTopicVO.getTopicDesc());
            dataAssetTopic.setStatusCode(dataAssetTopicVO.getStatusCode());
            dataAssetTopic.setCreateBy(dataAssetTopicVO.getCreateBy());
            dataAssetTopic.setCreateTime(dataAssetTopicVO.getCreateTime());
            dataAssetTopic.setUpdateBy(dataAssetTopicVO.getUpdateBy());
            dataAssetTopic.setUpdateTime(dataAssetTopicVO.getUpdateTime());

            return dataAssetTopic;
        }
        return null;
    }
}
