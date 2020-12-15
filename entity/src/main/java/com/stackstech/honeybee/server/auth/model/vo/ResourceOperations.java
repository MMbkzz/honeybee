package com.stackstech.honeybee.server.auth.model.vo;

import com.stackstech.honeybee.server.auth.model.AuthOperation;
import com.stackstech.honeybee.server.auth.model.AuthResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色配置权限是的结构树的节点对象
 */
public class ResourceOperations implements Serializable {
    /**
     * 资源
     */
    private AuthResource resource;
    /**
     * 操作 list
     */
    private List<AuthOperation> operations;
    /**
     * 子节点 list
     */
    private List<ResourceOperations> children = new ArrayList<ResourceOperations>(0);

    public AuthResource getResource() {
        return resource;
    }

    public void setResource(AuthResource resource) {
        this.resource = resource;
    }

    public List<AuthOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<AuthOperation> operations) {
        this.operations = operations;
    }

    public List<ResourceOperations> getChildren() {
        return children;
    }

    public void setChildren(List<ResourceOperations> children) {
        this.children = children;
    }

}
