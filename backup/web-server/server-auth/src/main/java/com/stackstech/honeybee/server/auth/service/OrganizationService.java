package com.stackstech.honeybee.server.auth.service;

import com.stackstech.honeybee.server.auth.model.AuthOrganization;
import com.stackstech.honeybee.server.auth.model.vo.OrganizationUserVo;
import com.stackstech.honeybee.server.auth.tree.OrganizationTreeBuilder;

import java.util.List;

/**
 * 组织结构
 */
public interface OrganizationService {
    /**
     * 新增组织
     *
     * @param organization
     * @return
     */
    String addOrganization(AuthOrganization organization);

    /**
     * 删除组织
     *
     * @param id
     * @return
     */
    String delOrganization(Long id);

    /**
     * 编辑组织
     *
     * @param organization
     * @return
     */
    String editOrganization(AuthOrganization organization);

    /**
     * 获取组织下的子组织和用户
     *
     * @param id
     * @return
     */
    OrganizationUserVo getOrganizationUserVo(Long id);

    /**
     * 获取组织树
     *
     * @param id
     * @return
     */
    List<OrganizationTreeBuilder.Node> getOrganizationTreeList(String id);

}
