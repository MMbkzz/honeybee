package com.stackstech.dcp.server.auth.model.vo;

import com.stackstech.dcp.server.auth.model.AuthOrganization;
import com.stackstech.dcp.server.auth.model.AuthUser;

import java.util.List;

/**
 * 组织下用户和子组织
 */
public class OrganizationUserVo {
    /**
     * 组织集合
     */
    private List<AuthOrganization> organizations;
    /**
     * 用户集合
     */
    private List<AuthUser> users;

    public List<AuthOrganization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<AuthOrganization> organizations) {
        this.organizations = organizations;
    }

    public List<AuthUser> getUsers() {
        return users;
    }

    public void setUsers(List<AuthUser> users) {
        this.users = users;
    }

}
