package com.stackstech.dcp.server.auth.tree;

import com.stackstech.dcp.server.auth.model.AuthCategory;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class BuildCategoryTree {

    List<AuthCategory> nodes = new ArrayList<AuthCategory>();

    public BuildCategoryTree(List<AuthCategory> nodes) {
        this.nodes = nodes;
    }

    /**
     * 构建树形结构
     */
//    public List<AuthCategory> buildTree() {
//        List<AuthCategory> treeNodes = new ArrayList<AuthCategory>();
//        List<AuthCategory> rootNodes = getRootNodes();
//        for (AuthCategory rootNode : rootNodes) {
//            buildChildNodes(rootNode);
//            treeNodes.add(rootNode);
//        }
//        return treeNodes;
//    }

    /**
     * 递归子节点
     *
     * @param node
     */
//    public void buildChildNodes(AuthCategory node) {
//        List<AuthCategory> children = getChildNodes(node);
//        if (!children.isEmpty()) {
//            for (AuthCategory child : children) {
//                buildChildNodes(child);
//            }
//            node.setChildren(children);
//        }
//    }

    /**
     * 获取父节点下所有的子节点
     */
//    public List<AuthCategory> getChildNodes(AuthCategory pnode) {
//        List<AuthCategory> childNodes = new ArrayList<AuthCategory>();
//        for (AuthCategory n : nodes) {
//            if (pnode.getId().equals(n.getParentId())) {
//                childNodes.add(n);
//            }
//        }
//        return childNodes;
//    }

    /**
     * 判断是否为根节点
     */
//    public boolean rootNode(AuthCategory node) {
//        boolean isRootNode = true;
//        for (AuthCategory n : nodes) {
//            if (node.getParentId().equals(n.getId())) {
//                isRootNode = false;
//                break;
//            }
//        }
//        return isRootNode;
//    }

    /**
     * 获取集合中所有的根节点
     * @return
     */
//    public List<AuthCategory> getRootNodes() {
//        List<AuthCategory> rootNodes = new ArrayList<AuthCategory>();
//        for (AuthCategory n : nodes) {
//            if (rootNode(n)) {
//                rootNodes.add(n);
//            }
//        }
//        return rootNodes;
//    }

}
