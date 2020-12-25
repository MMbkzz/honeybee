package com.stackstech.honeybee.server.auth.service.impl;

import com.stackstech.honeybee.core.util.SnowFlake;
import com.stackstech.honeybee.server.auth.dao.AuthOrgUserMapper;
import com.stackstech.honeybee.server.auth.dao.AuthOrganizationMapper;
import com.stackstech.honeybee.server.auth.dao.AuthUserMapper;
import com.stackstech.honeybee.server.auth.model.AuthOrgUser;
import com.stackstech.honeybee.server.auth.model.AuthOrganization;
import com.stackstech.honeybee.server.auth.model.AuthUser;
import com.stackstech.honeybee.server.auth.model.vo.OrganizationUserVo;
import com.stackstech.honeybee.server.auth.service.OrganizationService;
import com.stackstech.honeybee.server.auth.tree.OrganizationTreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 组织操作业务逻辑类
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private AuthOrganizationMapper authOrganizationMapper;
    @Autowired
    private AuthOrgUserMapper authOrgUserMapper;
    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private SnowFlake snowflake;

    /**
     * 新增组织
     *
     * @param organization
     * @return
     */
    @Override
    public String addOrganization(AuthOrganization organization) {
        AuthOrganization organizationGet = authOrganizationMapper.selectByName(organization.getName());
        if (organizationGet != null) {
            return "已经存在相同名称的组织";
        }
        organization.setId(snowflake.next());
        organization.setCreateTime(new Date());
        authOrganizationMapper.insert(organization);
        return "success";
    }

    /**
     * 删除组织
     *
     * @param id
     * @return
     */
    @Override
    public String delOrganization(Long id) {
        List<AuthOrganization> organizations = authOrganizationMapper.selectByParentId(id);
        if (organizations.size() > 0) {
            return "该组织下存在子组织不能删除";
        }
        List<AuthOrgUser> orgUsers = authOrgUserMapper.selectByOrgId(id);
        if (orgUsers.size() > 0) {
            return "该组织下存在用户不能删除";
        }
        authOrganizationMapper.deleteByPrimaryKey(id);
        return "success";
    }

    /**
     * 编辑组织
     *
     * @param organization
     * @return
     */
    @Override
    public String editOrganization(AuthOrganization organization) {
        if (!StringUtils.isEmpty(organization.getName())) {
            AuthOrganization organizationGet = authOrganizationMapper.selectByName(organization.getName());
            if (organizationGet != null && organizationGet.getId().longValue() != organization.getId().longValue()) {
                return "已经存在相同名称的组织";
            }
            if (organizationGet != null && organizationGet.getName().equals(organization.getName())) {
                organization.setName(null);
            }
        }
        authOrganizationMapper.updateByPrimaryKeySelective(organization);
        return "success";
    }

    /**
     * 获取组织下的子组织和用户
     *
     * @param id
     * @return
     */
    @Override
    public OrganizationUserVo getOrganizationUserVo(Long id) {
        OrganizationUserVo organizationUserVo = new OrganizationUserVo();
        List<AuthOrganization> organizations = authOrganizationMapper.selectByParentId(id);
        organizationUserVo.setOrganizations(organizations);
        List<AuthUser> users = authUserMapper.selectByOrgId(id);
        organizationUserVo.setUsers(users);
        return organizationUserVo;
    }

    /**
     * 获取组织树
     *
     * @param id
     * @return
     */
    @Override
    public List<OrganizationTreeBuilder.Node> getOrganizationTreeList(String id) {
        List<AuthOrganization> list = authOrganizationMapper.getOrganizationTreeList(id);
        List<OrganizationTreeBuilder.Node> nodes = new ArrayList<OrganizationTreeBuilder.Node>();

        for (AuthOrganization org : list) {
            OrganizationTreeBuilder.Node node = new OrganizationTreeBuilder.Node();
            node.setId(org.getId());
            node.setName(org.getName());
            node.setParentId(org.getParentId());
            node.setDescr(org.getDescr());
            nodes.add(node);
        }

        OrganizationTreeBuilder organizationTreeBuilder = new OrganizationTreeBuilder(nodes);
        List<OrganizationTreeBuilder.Node> nodeList = organizationTreeBuilder.buildTree();
        return nodeList;
    }

}
