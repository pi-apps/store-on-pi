package com.api.service.db.mysql.service;

import com.api.service.db.entity.TCmsMenuEntity;
import com.api.service.db.mysql.util.menu.MenuTree;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Set;


public interface ITCMSMenuService extends IService<TCmsMenuEntity> {

    List<TCmsMenuEntity> getAllMenuTree(String userId);

    List<TCmsMenuEntity> getMenuTree(String userId);

    List<TCmsMenuEntity> menuByUserId(String userId);

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId   父菜单ID
     * @param menuIdList 用户菜单ID
     */
    List<TCmsMenuEntity> queryListParentId(String parentId, List<String> menuIdList);

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     */
    List<TCmsMenuEntity> queryListParentId(String parentId);

    Set<String> permsByUserId(String userId);

    MenuTree selectList();

    List<TCmsMenuEntity> select();

    /**
     * 获取不包含按钮的菜单列表
     */
    List<TCmsMenuEntity> queryNotButtonList();

    void save(TCmsMenuEntity menu);

    void update(TCmsMenuEntity menu);

    void delete(String menuId);
    /**
     * 菜单
     */
    MenuTree selectListByUserId(String userId);

}
