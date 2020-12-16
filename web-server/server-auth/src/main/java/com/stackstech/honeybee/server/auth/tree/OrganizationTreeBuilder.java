package com.stackstech.honeybee.server.auth.tree;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;


public class OrganizationTreeBuilder {

    private final List<Node> nodes;


    public OrganizationTreeBuilder(List<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * 构建树形结构
     */
    public List<Node> buildTree() {
        List<Node> treeNodes = Lists.newArrayList();
        List<Node> rootNodes = getRootNodes();
        for (Node rootNode : rootNodes) {
            buildChildNodes(rootNode);
            treeNodes.add(rootNode);
        }
        return treeNodes;
    }

    /**
     * 递归子节点
     *
     * @param node
     */
    public void buildChildNodes(Node node) {
        List<Node> children = getChildNodes(node);
        if (!children.isEmpty()) {
            for (Node child : children) {
                buildChildNodes(child);
            }
            node.setChild(children);
        }
    }

    /**
     * 获取父节点下所有的子节点
     */
    public List<Node> getChildNodes(Node pnode) {
        List<Node> childNodes = Lists.newArrayList();
        for (Node n : nodes) {
            if (pnode.getId().equals(n.getParentId())) {
                childNodes.add(n);
            }
        }
        return childNodes;
    }

    /**
     * 判断是否为根节点
     */
    public boolean rootNode(Node node) {
        boolean isRootNode = true;
        for (Node n : nodes) {
            if (node.getParentId().equals(n.getId())) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    /**
     * 获取集合中所有的根节点
     *
     * @return
     */
    public List<Node> getRootNodes() {
        List<Node> rootNodes = Lists.newArrayList();
        for (Node n : nodes) {
            if (rootNode(n)) {
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }

    @Data
    public static class Node {
        private Long id;
        private String name;
        private String descr;
        private Long parentId;
        private List<Node> child;

    }

}
