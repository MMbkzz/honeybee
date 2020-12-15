package com.stackstech.dcp.server.auth.service.impl;

import com.google.common.collect.Maps;
import com.stackstech.dcp.core.util.SnowFlake;
import com.stackstech.dcp.server.auth.api.AuthCommon;
import com.stackstech.dcp.server.auth.dao.AuthPermissionMapper;
import com.stackstech.dcp.server.auth.dao.AuthResourceMapper;
import com.stackstech.dcp.server.auth.model.AuthPermission;
import com.stackstech.dcp.server.auth.model.AuthResource;
import com.stackstech.dcp.server.auth.model.vo.ResourceVo;
import com.stackstech.dcp.server.auth.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 资源操作业务逻辑类
 */
@Service
@Aspect
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private AuthResourceMapper resourceMapper;
    @Autowired
    private AuthPermissionMapper permissionMapper;

    @Autowired
    private SnowFlake snowflake;

    /**
     * 添加资源
     *
     * @param authResource
     * @return
     */
    @Override
    public int addResource(AuthResource authResource, Long UserId) {
        authResource.setId(snowflake.next());
        authResource.setCreateTime(new Date());
        authResource.setCreateBy(UserId);
        if (StringUtils.isEmpty(authResource.getStatus())) {
            authResource.setStatus(AuthCommon.AUTH_RESOURCE_STATUS_ENABLE);
        }
        if (authResource.getParentId() == null || authResource.getParentId() == 0) {
            //创建的资源是根节点
            authResource.setParentId(AuthCommon.AUTH_RESOURCE_ROOT_PID);
        }
        return resourceMapper.insertSelective(authResource);
    }

    /**
     * 删除资源极其子节点
     *
     * @param id
     * @return
     */
    @Override
    public int delResources(long id) {
        //查找children
        List<ResourceVo> resourceVos = getReourceByPid(id);
        //删除资源极其下面的子资源
        if (resourceVos.size() == 0) {
            return delResource(id);
        } else {
            for (ResourceVo e : resourceVos) {
                delResources(e.getId());
            }
        }
        return delResource(id);
    }

    @Override
    public String delResourcesById(long id) {
        // 判断是否有权限关联关系
        List<AuthPermission> permissions = permissionMapper.getPermissionsByResourceId(id);
        if (0 < permissions.size()) {
            return "该资源已关联角色.";
        }
        // 判断是否有子节点关系
        Map<String, Object> paramMap = new HashMap<String, Object>(0);
        paramMap.put("parent_id", id);
        List<AuthResource> resources = resourceMapper.getResourcesByCondition(paramMap);
        if (0 < resources.size()) {
            return "该资源存在子节点.";
        }
        // 删除该资源节点
        delResource(id);
        return "success";
    }

    /**
     * 获取子资源
     *
     * @param panrentId
     * @return
     */
    private List<ResourceVo> getReourceByPid(long panrentId) {
        return getReourceByPid(panrentId, 0, false);
    }

    /**
     * 获取子节点
     *
     * @param panrentId 父亲节点ID
     * @param depth     深度
     * @param flag      开关 是否使用深度
     * @return
     */
    private List<ResourceVo> getReourceByPid(long panrentId, int depth, boolean flag) {
        List<ResourceVo> list = new ArrayList<ResourceVo>();
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("parent_id", panrentId);
        List<AuthResource> resourcelist = resourceMapper.selectListByCondition(condition);
        for (AuthResource temp : resourcelist) {
            ResourceVo vo = Tansfer2ReourceVo(temp);
            list.add(vo);
        }
        if (flag) {
            depth++;
        }
        return list;
    }

    /**
     * 将 AuthResource 转化为 ResourceVo
     *
     * @param authResource
     * @return
     */
    private ResourceVo Tansfer2ReourceVo(AuthResource authResource) {
        ResourceVo resourceVo = new ResourceVo();
        resourceVo.setId(authResource.getId());
        resourceVo.setParent_id(authResource.getParentId());
        resourceVo.setDescr(authResource.getDescr());
        resourceVo.setStatus(authResource.getStatus());
        resourceVo.setCreateTime(authResource.getCreateTime());
        resourceVo.setUpdateTime(authResource.getUpdateTime());
        resourceVo.setCategory_id(String.valueOf(authResource.getCategoryId()));
        resourceVo.setName(authResource.getName());
        resourceVo.setCode(authResource.getCode());
//        resourceVo.setAuthOperation(authResource.getOperation());
        resourceVo.setAttr1(authResource.getAttr1());
        resourceVo.setAttr2(authResource.getAttr2());
        resourceVo.setAttr3(authResource.getAttr3());
        resourceVo.setAttr4(authResource.getAttr4());
        resourceVo.setAttr5(authResource.getAttr5());
        return resourceVo;
    }

    /**
     * 删除资源节点
     *
     * @param id
     * @return
     */
    private int delResource(long id) {
        return resourceMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新资源
     *
     * @param authResource
     * @return
     */
    @Override
    public int updateResource(AuthResource authResource, Long UserId) {
        authResource.setCreateBy(UserId);
        authResource.setCreateTime(new Date());
        authResource.setUpdateTime(new Date());
        authResource.setUpdateBy(UserId);
        return resourceMapper.updateByPrimaryKeySelective(authResource);
    }

    /**
     * 更新资源状态
     *
     * @param id
     * @param status
     * @param userId
     * @return
     */
    @Override
    public int updateResourceStatus(long id, String status, long userId) {
        AuthResource authResource = new AuthResource();
        authResource.setId(id);
        authResource.setStatus(status);
        authResource.setCreateBy(userId);
        authResource.setCreateTime(new Date());
        authResource.setUpdateTime(new Date());
        authResource.setUpdateBy(userId);
        return resourceMapper.updateByPrimaryKeySelective(authResource);
    }

    /**
     * 根据编码查找资源
     *
     * @param code
     * @return
     */
    @Override
    public List<AuthResource> getResourceByCode(String code, long id) {
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("code", code);
        condition.put("id", id);
        return resourceMapper.getResourcesByCondition(condition);
    }

    /**
     * 获取资源树
     *
     * @return
     */
    @Override
    public List<ResourceVo> getResourceTree() {
        List<ResourceVo> resultData = new ArrayList<ResourceVo>();
        List<AuthResource> rootList = getRootResource();
        //2. 遍历一级资源点,获取子节点
        for (AuthResource temp : rootList) {
            ResourceVo vo = Tansfer2ReourceVo(temp);
            //树的深度
            resultData.add(vo);
        }
        for (ResourceVo node : resultData) {
            getResource(node, 0, resultData);
        }
        return resultData;
    }

    /**
     * 获取一级资源节点
     *
     * @return
     */
    private List<AuthResource> getRootResource() {
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("parent_id", AuthCommon.AUTH_RESOURCE_ROOT_PID);
        List<AuthResource> rootList = resourceMapper.selectListByCondition(condition);
        return rootList;
    }

    /**
     * 获取所有的资源树
     *
     * @return
     */
    private void getResource(ResourceVo node, int depth, List<ResourceVo> resultData) {
        List<ResourceVo> children = new ArrayList<ResourceVo>();
        children = getReourceByPid(node.getId(), depth, true);
        if (children.size() == 0) {
            return;
        } else {
            depth++;
            node.setDepth(depth);
            node.setChildren(children);
            for (ResourceVo temp : children) {
                getResource(temp, depth, resultData);
            }
        }
    }

}
