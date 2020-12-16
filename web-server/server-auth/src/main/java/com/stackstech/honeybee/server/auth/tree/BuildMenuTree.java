package com.stackstech.honeybee.server.auth.tree;

import com.stackstech.honeybee.server.auth.api.AuthCommon;
import com.stackstech.honeybee.server.auth.model.vo.MenuVo;

import java.util.ArrayList;
import java.util.List;

public class BuildMenuTree {
    private List<MenuVo> menus = new ArrayList<MenuVo>(0);
    private final List<MenuVo> excludeMenus = new ArrayList<MenuVo>(0);

    public BuildMenuTree(List<MenuVo> menus) {
        this.menus = menus;
    }

    public List<MenuVo> buildTree() {
        List<MenuVo> menu = getMenuRoot();
        return buildMenuChildren(menu);
    }

    public List<MenuVo> getMenuRoot() {
        List<MenuVo> menu = new ArrayList<>();
        for (MenuVo m : menus) {
            if (AuthCommon.AUTH_RESOURCE_ROOT_PID == m.getParent_id().longValue()) {
                menu.add(m);
            }
        }
        return menu;
    }

    public List<MenuVo> buildMenuChildren(List<MenuVo> parent) {
        List<MenuVo> menu = new ArrayList<>();

        for (MenuVo m : parent) {
            menu.add(buildMenuChildren(m));
        }
        return menu;
    }

    public MenuVo buildMenuChildren(MenuVo parent) {
        for (MenuVo m : menus) {
            if (parent.getId().equals(m.getParent_id())) {
                parent.getChildren().add(buildMenuChildren(m));
            }
        }

        return parent;
    }

}
