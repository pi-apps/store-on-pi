package com.api.service.db.mysql.util.menu;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 属性菜单工具类
 */
public class MenuTreeUtils {
    /**
     * 递归树
     *
     * @param dtos
     * @param rootTree
     * @return
     */
    public static MenuTree packageTree(List<Map<String, Object>> dtos, MenuTree rootTree) {
        if (dtos != null && dtos.size() > 0) {
            dtos.forEach(menu -> judgeObj(menu, rootTree));
            if (rootTree.getList() != null && rootTree.getList().size() > 0) {
                List<MenuTree> children = rootTree.getList();
                children = children.stream().sorted(Comparator.comparing(MenuTree::getOrderNum)).collect(Collectors.toList());
                children.forEach(r -> packageTree(dtos, r));
            } else {
                rootTree.setLastLevel(true);
            }
        }
        return rootTree;
    }

    /**
     * 组装子元素
     *
     * @param dto
     * @param rootTree
     */
    public static void judgeObj(Map<String, Object> dto, MenuTree rootTree) {
        List<MenuTree> children = rootTree.getList();
        if (children == null) {
            children = new ArrayList<>();
        }
        if (dto.get("parentId") != null && rootTree.getMenuId().equals(dto.get("parentId"))) {
            children.add(MenuTree.builder().menuId((String) dto.get("menuId")).menuName((String) dto.get("menuName")).
                    icon((String) dto.get("icon")).parentId((String) dto.get("parentId")).type((Integer) dto.get("type"))
                    .menuUrl((String) dto.get("menuUrl")).orderNum((Integer) dto.get("orderNum"))
                    .perms((String) dto.get("perms"))
                    .infc((String) dto.get("perms")).build());
            rootTree.setList(children);
        }
    }
}
