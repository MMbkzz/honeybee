package com.stackstech.honeybee.server.auth.tree;

import com.google.common.collect.Lists;
import com.stackstech.honeybee.server.auth.api.AuthCommon;
import com.stackstech.honeybee.server.auth.model.vo.ResourceOperations;

import java.util.List;

public class BuildPermissionTree {
    private List<ResourceOperations> resourceOperationsList = Lists.newArrayList();
    private final List<ResourceOperations> excludeRootList = Lists.newArrayList();

    public BuildPermissionTree(List<ResourceOperations> resourceOperationsList) {
        this.resourceOperationsList = resourceOperationsList;
    }

    public List<ResourceOperations> buildTree() {
        List<ResourceOperations> resultList = Lists.newArrayList();
        for (ResourceOperations op : resourceOperationsList) {
            ResourceOperations temp = getRootResourceOperations();
            if (null != temp) {
                excludeRootList.add(temp);
                resultList.add(buildResourceOperationsChildren(temp));
            }
        }
        return resultList;
    }

    public ResourceOperations getRootResourceOperations() {
        for (ResourceOperations resourceOperations : resourceOperationsList) {
            if (!isHasExecutedRoot(resourceOperations) && AuthCommon.AUTH_RESOURCE_ROOT_PID == resourceOperations.getResource().getParentId().longValue()) {
                return resourceOperations;
            }
        }
        return null;
    }

    public boolean isHasExecutedRoot(ResourceOperations op) {
        return excludeRootList.contains(op);
    }

    public ResourceOperations buildResourceOperationsChildren(ResourceOperations parent) {
        for (ResourceOperations resourceOperations : resourceOperationsList) {
            if (parent.getResource().getId().equals(resourceOperations.getResource().getParentId())) {
                parent.getChildren().add(buildResourceOperationsChildren(resourceOperations));
            }
        }
        return parent;
    }

}
