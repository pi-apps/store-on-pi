package com.api.service.db.mysql.service.impl;

import com.api.service.AdminName;
import com.api.service.UUID;
import com.api.service.db.mysql.BaseMysqlTable;
import com.api.service.db.mysql.mapper.TCMSMenuMapper;
import com.api.service.db.mysql.service.ITCMSMenuService;
import com.api.service.db.mysql.service.ITCMSUserService;
import com.api.service.db.mysql.sql.service.ISysSqlService;
import com.api.service.db.mysql.util.beanToMap.BeanMapUtil;
import com.api.service.db.mysql.util.menu.Menu;
import com.api.service.db.mysql.util.menu.MenuTree;
import com.api.service.db.mysql.util.menu.MenuTreeUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @author Ashy.Cheung
 * @date 2020.07.20
 */

@Service("ITCMSMenuService")
public class TCMSMenuServiceImpl extends ServiceImpl<TCMSMenuMapper, TCmsMenuEntity> implements ITCMSMenuService {

    @Autowired
    private ITCMSUserService itUserService;
    @Autowired
    private ISysSqlService iSysSqlService;

    /**
     * 获取用户拥有角色下所有的菜单和接口(整表全量数据)
     *
     * @param userId
     * @return
     */

    @Override
    public List<TCmsMenuEntity> getAllMenuTree(String userId) {
        List<TCmsMenuEntity> allMenu;
        if (AdminName.getName.equals(userId)) { //超管
            Wrapper<TCmsMenuEntity> wrapper1 = new EntityWrapper<TCmsMenuEntity>()
                    .where("1=1");
            //将所有的菜单一次取出
            allMenu = this.baseMapper.selectList(wrapper1);
        } else {
            //查询当前用户菜单ID
            List<String> allMenuId = itUserService.queryAllMenuId(userId);
            if (null == allMenuId || allMenuId.isEmpty()) {
                return null;

            }
            //获取当前用户所有菜单及接口对象
            allMenu = this.baseMapper.selectBatchIds(allMenuId);
        }
        // 定义最后的结果
        List<TCmsMenuEntity> menuList = new ArrayList<>();
        // 先把第一级菜单全部找到
        for (int i = 0; i < allMenu.size(); i++) {
            // 一级菜单的parentId 为 0
            if ("0".equals(allMenu.get(i).getParentId())) {
                menuList.add(allMenu.get(i));//将一级菜单装入结果集
            }
        }
        for (TCmsMenuEntity menu : menuList) { //遍历一级菜单
            menu.setChildMenus(getChild(menu.getMenuId(), allMenu));//getChild是递归调用的
        }
        return menuList;
    }

    /**
     * 获取用户拥有的角色下所有的菜单(纯菜单,提供给userInfo接口,用户控制台菜单页面)
     *
     * @param userId
     * @return
     */
    @Override
    public List<TCmsMenuEntity> getMenuTree(String userId) {
        List<TCmsMenuEntity> allMenu;
        if (AdminName.getName.equals(userId)) { //超管
            Wrapper<TCmsMenuEntity> wrapper1 = new EntityWrapper<TCmsMenuEntity>()
                    .where("1=1")
                    .eq("infc", "0");//菜单
            //将所有的菜单一次取出
            allMenu = this.baseMapper.selectList(wrapper1);
        } else {
            //查询当前用户菜单ID,用户-->多角色-->去重复多菜单集合
            List<String> allMenuId = itUserService.queryAllMenuId(userId);
            if (null == allMenuId || allMenuId.isEmpty()) {
                return null;

            }
            allMenu = new ArrayList<>(); //声明对象
            //获取当前用户所有菜单及接口对象
            List<TCmsMenuEntity> list = this.baseMapper.selectBatchIds(allMenuId);
            for (TCmsMenuEntity t : list) {
                if ("0".equals(t.getInfc())) {
                    allMenu.add(t);
                }
            }
        }
        // 定义最后的结果
        List<TCmsMenuEntity> menuList = new ArrayList<>();
        // 先把第一级菜单全部找到
        for (int i = 0; i < allMenu.size(); i++) {
            // 一级菜单的parentId 为 0
            if ("0".equals(allMenu.get(i).getParentId())) {
                menuList.add(allMenu.get(i));//将一级菜单装入结果集
            }
        }
        for (TCmsMenuEntity menu : menuList) { //遍历一级菜单
            menu.setChildMenus(getChild(menu.getMenuId(), allMenu));//getChild是递归调用的
        }
        return menuList;
    }

    @Override
    public List<TCmsMenuEntity> menuByUserId(String userId) {
        //系统管理员，拥有最高权限
        if ((AdminName.getName).equals(userId)) {
            return this.getAllMenuList(null);
        }

        //用户菜单列表
        List<String> menuIdList = itUserService.queryAllMenuId(userId);
        if (null == menuIdList) {
            throw new RuntimeException("权限不足，请先找管理员开通！");
        }
        return this.getAllMenuList(menuIdList);
    }

    @Override
    public List<TCmsMenuEntity> queryListParentId(String parentId, List<String> menuIdList) {
        List<TCmsMenuEntity> menuList = this.queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }

        List<TCmsMenuEntity> userMenuList = new ArrayList<>();
        for (TCmsMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getMenuId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<TCmsMenuEntity> queryListParentId(String parentId) {
        Wrapper<TCmsMenuEntity> wrapper = new EntityWrapper<TCmsMenuEntity>()
                .where("1=1")
                .eq("parent_id", parentId)
                .orderBy("`order_num` ASC");
        return this.selectList(wrapper);
    }

    @Override
    public Set<String> permsByUserId(String userId) {
        Set<String> permsSet = new HashSet<>();
        if (AdminName.getName.equals(userId)) {
            Wrapper<TCmsMenuEntity> wrapper = new EntityWrapper<TCmsMenuEntity>()
                    .where("1=1")
                    .isNotNull("perms");
            List<TCmsMenuEntity> menuList = this.selectList(wrapper);
            for (TCmsMenuEntity menu : menuList) {
                permsSet.addAll(Arrays.asList(menu.getPerms().trim().split(",")));
            }
        } else {
            String sql = "SELECT m.perms AS var1 FROM " + BaseMysqlTable.USER_ROLE_TABLE + " ur LEFT JOIN " + BaseMysqlTable.ROLE_MENU_TABLE + " rm ON ur.role_id = rm.role_id LEFT JOIN " + BaseMysqlTable.MENU_TABLE + " m ON rm.menu_id = m.menu_id WHERE m.perms IS NOT NULL AND ur.user_id = '" + userId + "'";
            List<Map<String, Object>> listMap = iSysSqlService.selectSql(sql);
            for (Map<String, Object> map : listMap) {
                String val = (String) map.get("var1");
                permsSet.addAll(Arrays.asList(val.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public MenuTree selectList() {
        List<TCmsMenuEntity> menuList = this.selectList(null);
        List<Map<String, Object>> menuListMap = new ArrayList<>();
        for (TCmsMenuEntity e : menuList) {
            menuListMap.add(BeanMapUtil.convertBean(e));
        }
        MenuTree rootTree = new MenuTree();
        rootTree.setMenuId("0");
        MenuTree menuTree = MenuTreeUtils.packageTree(menuListMap, rootTree);
        return menuTree;
    }

    @Override
    public List<TCmsMenuEntity> select() {
        //查询列表数据
        List<TCmsMenuEntity> menuList = this.queryNotButtonList();
        //添加顶级菜单
        TCmsMenuEntity root = new TCmsMenuEntity();
        root.setMenuId("0");
        root.setMenuName("一级菜单");
        root.setParentId("-1");
        menuList.add(root);
        return menuList;
    }

    @Override
    public List<TCmsMenuEntity> queryNotButtonList() {
        Wrapper<TCmsMenuEntity> wrapper = new EntityWrapper<TCmsMenuEntity>()
                .where("1=1")
                .ne("type", 2) //不等于
                .orderBy("`order_num` ASC");
        return this.selectList(wrapper);
    }

    @Override
    public void save(TCmsMenuEntity menu) {
        this.verifyForm(menu);
        menu.setMenuId(UUID.randomUUID());
        this.insert(menu);
    }

    @Override
    public void update(TCmsMenuEntity menu) {
        this.verifyForm(menu);
        this.updateById(menu);
    }

    @Override
    public void delete(String menuId) {
        //删除菜单
        this.deleteById(menuId);
    }

    @Override
    public MenuTree selectListByUserId(String userId) {
        //获取当前用户所有菜单id
        List<String> menuIdList = itUserService.queryAllMenuId(userId);
        List<TCmsMenuEntity> menuList = this.selectBatchIds(menuIdList);
        List<Map<String, Object>> menuListMap = new ArrayList<>();
        for (TCmsMenuEntity e : menuList) {
            menuListMap.add(BeanMapUtil.convertBean(e));
        }
        MenuTree rootTree = new MenuTree();
        rootTree.setMenuId("0");
        MenuTree menuTree = MenuTreeUtils.packageTree(menuListMap, rootTree);
        return menuTree;
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(TCmsMenuEntity menu) {
        if (StringUtils.isBlank(menu.getMenuName())) {
            //throw new RRException("菜单名称不能为空");
            throw new RuntimeException("菜单名称不能为空");
        }
        if (menu.getParentId() == null) {
            //	throw new RRException("上级菜单不能为空");
            throw new RuntimeException("上级菜单不能为空");
        }
        //菜单
        if (menu.getType() == Menu.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getMenuUrl())) {
                //	throw new RRException("菜单URL不能为空");
                throw new RuntimeException("菜单URL不能为空");
            }
        }
        //上级菜单类型
        int parentType = Menu.MenuType.CATALOG.getValue();
        if (!"0".equals(menu.getParentId())) {
            TCmsMenuEntity parentMenu = this.selectById(menu.getParentId());
            parentType = parentMenu.getType();
        }
        //目录、菜单
        if (menu.getType() == Menu.MenuType.CATALOG.getValue() ||
                menu.getType() == Menu.MenuType.MENU.getValue()) {
            if (parentType != Menu.MenuType.CATALOG.getValue()) {
                throw new RuntimeException("上级菜单只能为目录类型");
            }
            return;
        }
        //按钮
        if (menu.getType() == Menu.MenuType.BUTTON.getValue()) {
            if (parentType != Menu.MenuType.MENU.getValue()) {
                throw new RuntimeException("上级菜单只能为菜单类型");
            }
        }
    }

    /**
     * 获取所有菜单列表
     */
    private List<TCmsMenuEntity> getAllMenuList(List<String> menuIdList) {
        //查询根菜单列表
        List<TCmsMenuEntity> menuList = this.queryListParentId("0", menuIdList);
        //递归获取子菜单
        this.getMenuTreeList(menuList, menuIdList);
        return menuList;
    }

    /**
     * 递归
     */
    private List<TCmsMenuEntity> getMenuTreeList(List<TCmsMenuEntity> menuList, List<String> menuIdList) {
        List<TCmsMenuEntity> subMenuList = new ArrayList<>();

        for (TCmsMenuEntity entity : menuList) {
            //目录
            if (entity.getType() == 0) {
                entity.setChildMenus(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }


    private List<TCmsMenuEntity> getChild(String id, List<TCmsMenuEntity> rootMenu) {
        // 子菜单
        List<TCmsMenuEntity> childList = new ArrayList<>();
        for (TCmsMenuEntity menu : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (!"0".equals(menu.getParentId())) {
                if (menu.getParentId().equals(id)) {
                    childList.add(menu);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (TCmsMenuEntity menu : childList) {
            // 递归
            menu.setChildMenus(getChild(menu.getMenuId(), rootMenu));
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }
}
