package com.stackstech.dcp.server.auth.service;

import com.stackstech.dcp.server.auth.model.AuthResource;
import com.stackstech.dcp.server.auth.model.vo.ResourceVo;

import java.util.List;

/**
 * 资源
 */
public interface ResourceService {
    /**
     * 添加资源
     *
     * @param authResource
     * @return
     */
    int addResource(AuthResource authResource, Long UserId);

    /**
     * 删除资源极其子节点
     *
     * @param id
     * @return
     */
    int delResources(long id);

    /**
     * 通过资源 id 删除资源
     *
     * @param id
     * @return
     */
    String delResourcesById(long id);

    /**
     * 更新资源
     *
     * @param authResource
     * @return
     */
    int updateResource(AuthResource authResource, Long UserId);

    /**
     * 更新资源状态
     *
     * @param id
     * @param status
     * @param userId
     * @return
     */
    int updateResourceStatus(long id, String status, long userId);

    /**
     * 根据编码查找资源
     *
     * @param code
     * @return
     */
    List<AuthResource> getResourceByCode(String code, long id);

    /**
     * 获取资源树
     *
     * @return
     */
    List<ResourceVo> getResourceTree();

}
