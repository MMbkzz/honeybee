package com.stackstech.dcp.server.platform.service.impl;

import com.stackstech.dcp.core.cache.*;
import com.stackstech.dcp.core.enums.InstanceStageEnum;
import com.stackstech.dcp.core.enums.InstanceStatusEnum;
import com.stackstech.dcp.core.http.ResponseError;
import com.stackstech.dcp.core.http.ResponseOk;
import com.stackstech.dcp.core.model.LoginUserProtos;
import com.stackstech.dcp.server.auth.dao.AuthUserMapper;
import com.stackstech.dcp.server.auth.model.AuthUser;
import com.stackstech.dcp.server.auth.utils.LoginUserManager;
import com.stackstech.dcp.server.auth.utils.TokenUtil;
import com.stackstech.dcp.server.dataasset.dao.ModelCodeMapper;
import com.stackstech.dcp.server.dataasset.model.ModelCode;
import com.stackstech.dcp.server.datasource.dao.ServiceSourceMapper;
import com.stackstech.dcp.server.datasource.model.ServiceSource;
import com.stackstech.dcp.server.platform.dao.InstanceMapper;
import com.stackstech.dcp.server.platform.dao.InstanceResourceMapper;
import com.stackstech.dcp.server.platform.model.Instance;
import com.stackstech.dcp.server.platform.model.InstanceResource;
import com.stackstech.dcp.server.platform.service.InstanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InstanceServiceImpl implements InstanceService {

    @Autowired
    private InstanceHeartbeatCache instanceHeartbeatCache;
    @Autowired
    private ResourceExpectCache resourceExpectCache;
    @Autowired
    private ResourceExpectStatusCache resourceExpectStatusCache;
    @Autowired
    private InstanceOnlineCache instanceOnlineCache;
    @Autowired
    private InstanceOfflineCache instanceOfflineCache;
    @Autowired
    private ResourceHoldCache resourceHoldCache;

    @Autowired
    private InstanceMapper instanceMapper;
    @Autowired
    private InstanceResourceMapper instanceResourceMapper;
    @Autowired
    private ServiceSourceMapper serviceSourceMapper;
    @Autowired
    private ModelCodeMapper modelCodeMapper;

    @Autowired
    private LoginUserManager loginUserManager;
    @Autowired
    private AuthUserMapper authUserMapper;

    /**
     * 获取集群列表
     *
     * @param instance
     * @return
     * @throws Exception
     */
    @Override
    public List<Instance> queryAll(Instance instance) throws Exception {
        List<Instance> instances = null;
        if (StringUtils.isNotBlank(instance.getQueryType())) {
            instances = instanceMapper.queryByCondition(instance.getQueryString());
        } else {
            instances = instanceMapper.queryAll(instance);
        }
        if (instances != null && instances.size() > 0) {
            for (Instance ins : instances) {
                //获取更新用户
                this.queryUpdateUser(ins);
            }
        }
        return instances;
    }

    /**
     * 获取集群计数
     *
     * @param instance
     * @return
     * @throws Exception
     */
    @Override
    public int countAll(Instance instance) throws Exception {
        int i = 0;
        if (StringUtils.isNotBlank(instance.getQueryType())) {
            i = instanceMapper.countByCondition(instance.getQueryString());
        } else {
            i = instanceMapper.countAll(instance);
        }
        return i;
    }

    @Override
    public ResponseEntity<?> insert(Instance instance, HttpServletRequest req) throws Exception {
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            instance.setCreateBy(loginUser.getUserId());
            instance.setUpdateBy(loginUser.getUserId());
        }

        if (instance.getHost() != null && instance.getPort() != null) {
            Instance ins = instanceMapper.queryByHost(instance.getId(), instance.getHost(), instance.getPort());
            if (ins != null) {
                return ResponseError.create(500, "该实例已存在，请勿重复配置");
            }
        }
        int i = instanceMapper.insert(instance);
        if (i > 0) {
            return ResponseOk.create("实例新增成功");
        } else {
            return ResponseError.create(500, "实例新增失败");
        }
    }


    /**
     * 修改实例配置
     *
     * @param instance
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<?> update(Instance instance, HttpServletRequest req) throws Exception {
        LoginUserProtos.LoginUser loginUser = getLoginUser(req);
        if (loginUser != null) {
            instance.setUpdateBy(loginUser.getUserId());
        }

        int i = instanceMapper.update(instance);
        if (i > 0) {
            return ResponseOk.create("实例更新成功");
        } else {
            return ResponseError.create(500, "实例更新失败");
        }
    }

    /**
     * 修改状态
     *
     * @param instance
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<?> changeStatus(Instance instance, HttpServletRequest req) throws Exception {
        Instance ins = instanceMapper.queryByPrimaryKey(instance.getId());
        if (ins != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", ins.getStageCode());
            map.put("type", "instance_stage_code");
            //当实例状态statusCode='待停止' 进行重置操作  <停止/停止>
            if (InstanceStatusEnum.stopped.toString().equals(instance.getStatusCode()) && InstanceStageEnum.stopped.toString().equals(instance.getStageCode())) {
                if (InstanceStatusEnum.stopping.toString().equals(ins.getStatusCode())) {
                    //验证实例是否在重置中
                    List<InstanceResource> instanceResources = instanceResourceMapper.queryByInstanceId(instance.getId());
                    if (instanceResources != null && instanceResources.size() > 0) {
                        for (InstanceResource instanceResource : instanceResources) {
                            ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(instanceResource.getServiceSourceId());
                            if (serviceSource != null) {
                                Integer expectNum = resourceExpectCache.get(instance.getHost() + ":" + instance.getPort(), serviceSource.getId());
                                if (expectNum != null && expectNum != 0) {
                                    return ResponseError.create(500, "当前实例正在重置中!不可重复操作");
                                }
                            }
                        }
                    }
                    //释放已有缓存历史 & DB删除
                    String hostConfig = ins.getHost() + ":" + ins.getPort();
                    deallocStatusCache(hostConfig);
                    deallocResourceCache(hostConfig);
                    instanceResourceMapper.deleteByInstanceId(instance.getId());
                } else {
                    ModelCode modelCode = modelCodeMapper.queryCode(map);
                    String displayName = (modelCode == null ? ins.getStageCode() : modelCode.getDisplayName());
                    return ResponseError.create(500, "实例处于" + displayName + "阶段，不能进行重置操作");
                }
            }

            //当状态=“停止”and 阶段=“已下线”进行启动操作  <未知/未激活>
            if (InstanceStatusEnum.unknown.toString().equals(instance.getStatusCode()) &&
                    !(InstanceStatusEnum.stopped.toString().equals(ins.getStatusCode()) && InstanceStageEnum.stopped.toString().equals(ins.getStageCode()))) {
                ModelCode modelCode = modelCodeMapper.queryCode(map);
                String displayName = (modelCode == null ? ins.getStageCode() : modelCode.getDisplayName());
                return ResponseError.create(500, "实例处于" + displayName + "阶段，不能进行启用操作");
            }

            //当阶段=“已上线”进行停用操作  <待停止>
            if (InstanceStatusEnum.stopping.toString().equals(instance.getStatusCode()) && !InstanceStageEnum.online.toString().equals(ins.getStageCode())) {
                ModelCode modelCode = modelCodeMapper.queryCode(map);
                String displayName = (modelCode == null ? ins.getStageCode() : modelCode.getDisplayName());
                return ResponseError.create(500, "实例处于" + displayName + "阶段，不能进行停用操作");
            }

            //更新DB状态
            LoginUserProtos.LoginUser loginUser = getLoginUser(req);
            if (loginUser != null) {
                instance.setUpdateBy(loginUser.getUserId());
            }

            int i = instanceMapper.update(instance);
            if (i > 0) {
                return ResponseOk.create("实例状态更新成功");
            } else {
                return ResponseError.create(500, "实例状态更新失败");
            }
        } else {
            return ResponseError.create(500, "实例不存在");
        }
    }

    @Override
    public int delete(String id) throws Exception {
        Instance ins = instanceMapper.queryByPrimaryKey(id);
        if (ins == null) {
            return 0;
        }
        int i = instanceMapper.delete(id);
        if (i > 0) {
            //释放已有缓存历史 & DB删除
            String hostConfig = ins.getHost() + ":" + ins.getPort();
            deallocStatusCache(hostConfig);
            deallocResourceCache(hostConfig);
            instanceResourceMapper.deleteByInstanceId(id);
        }
        return i;
    }

    /**
     * 清空实例状态Cache
     */
    private void deallocStatusCache(String hostConfig) {
        if (StringUtils.isNotEmpty(hostConfig)) {
            // 释放缓存
            // 释放缓存 - 心跳包
            instanceHeartbeatCache.delete(null, hostConfig);
            // 释放缓存 -
            instanceOnlineCache.delete(null, hostConfig);
            // 释放缓存 -
            instanceOfflineCache.delete(null, hostConfig);
        }
    }

    /**
     * 释放实例中包含资源Cache
     */
    private void deallocResourceCache(String hostConfig) {
        if (StringUtils.isNotEmpty(hostConfig)) {
            // 释放缓存 - 预期资源
            resourceExpectCache.delete(hostConfig);
            // 释放缓存 - 持有资源
            resourceHoldCache.delete(hostConfig);
            // 释放缓存 - 预期资源执行状态
            resourceExpectStatusCache.delete(null, hostConfig);
        }
    }

    /**
     * 获取登录用户
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
     * 获取更新用户
     *
     * @param ins
     */
    private void queryUpdateUser(Instance ins) {
        AuthUser authUser = null;
        if (ins.getCreateBy() != null) {
            authUser = authUserMapper.selectByloginId(ins.getCreateBy());
            ins.setCreateUser(authUser != null ? authUser.getLoginName() : null);
        }
        if (ins.getUpdateBy() != null) {
            authUser = authUserMapper.selectByloginId(ins.getUpdateBy());
            ins.setUpdateUser(authUser != null ? authUser.getLoginName() : null);
        }
    }

    @Override
    public List<Instance> getWait4Online() {
        String[] status = {InstanceStatusEnum.normal.code, InstanceStatusEnum.unknown.code};
        String[] stage = {InstanceStageEnum.activated.code};

        return instanceMapper.queryByStatus(status, stage);
    }

    @Override
    public List<Instance> getWait4Offline() {
        String[] status = {InstanceStatusEnum.stopping.code};
        return instanceMapper.queryByStatus(status, null);
    }

    @Override
    public List<Instance> getUsable() {
        String[] status = {
                InstanceStatusEnum.normal.code
                , InstanceStatusEnum.unknown.code
        };
        String[] stage = {
                InstanceStageEnum.activated.code
                , InstanceStageEnum.initialized.code
                , InstanceStageEnum.registered.code
                , InstanceStageEnum.online.code
        };

        return instanceMapper.queryByStatus(status, stage);
    }

    @Override
    public Instance getByHost(String host) {
        if (org.apache.commons.lang.StringUtils.isBlank(host)) {
            return null;
        }
        String[] instance = host.split(":");
        return instanceMapper.queryByHost(null, instance[0], instance[1]);
    }

    @Override
    public List<Instance> getByServiceSourceId(String serviceSourceId) {
        if (org.apache.commons.lang.StringUtils.isBlank(serviceSourceId)) {
            return null;
        }
        return instanceMapper.queryByServiceSourceId(serviceSourceId);
    }

    @Override
    public void saveInstanceResource(String instanceId, String sourceId, Integer expectNumber) {
        if (org.apache.commons.lang.StringUtils.isBlank(instanceId)
                || org.apache.commons.lang.StringUtils.isBlank(sourceId)
                || null == expectNumber) {
            return;
        }
        instanceResourceMapper.delete(instanceId, sourceId);

        InstanceResource instanceResource = new InstanceResource();
        instanceResource.setExpectNumber(expectNumber);
        instanceResource.setInstanceId(instanceId);
        instanceResource.setServiceSourceId(sourceId);

        instanceResource.setResourceTypeCode("0");
        instanceResourceMapper.insert(instanceResource);
    }
}
