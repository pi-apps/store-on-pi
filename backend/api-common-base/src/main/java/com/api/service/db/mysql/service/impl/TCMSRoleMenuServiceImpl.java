package com.api.service.db.mysql.service.impl;

import com.api.service.MapUtils;
import com.api.service.UUID;
import com.api.service.db.mysql.mapper.TCMSRoleMenuMapper;
import com.api.service.db.mysql.service.ITCMSMenuService;
import com.api.service.db.mysql.service.ITCMSRoleMenuService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Ashy.Cheung
 * @date 2020.07.20
 */
@Service("ITCMSRoleMenuService")
public class TCMSRoleMenuServiceImpl extends ServiceImpl<TCMSRoleMenuMapper, TCmsRoleMenuEntity> implements ITCMSRoleMenuService {

    @Autowired
    private ITCMSMenuService itMenuService;


    /**
     * 根据角色主键,查询角色下的菜单集合
     *
     * @param roleId
     * @return
     */
    @Override
    public List<TCmsMenuEntity> queryMenuList(String roleId) {
        Wrapper<TCmsRoleMenuEntity> wrapper = new EntityWrapper<TCmsRoleMenuEntity>()
                .where("1=1")
                .eq("role_id", roleId)
                .setSqlSelect("menu_id");
        List<Object> list = this.selectObjs(wrapper);
        List<String> strs;
        if (null != list && 0 < list.size()) {
            strs = new ArrayList<>();
            for (Object o : list) {
                strs.add((String) o);
            }
            return itMenuService.selectBatchIds(strs);
        }
        return null;
    }

    @Override
//    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveOrUpdate(String roleId, List<String> menuIdList) {
        //先删除角色与菜单关系
        deleteBatch(new String[]{roleId});
        if (menuIdList.size() == 0) {
            return;
        }
        //保存角色与菜单关系
        List<TCmsRoleMenuEntity> list = new ArrayList<>(menuIdList.size());
        for (String menuId : menuIdList) {
            TCmsRoleMenuEntity sysRoleMenuEntity = new TCmsRoleMenuEntity();
            sysRoleMenuEntity.setId(UUID.randomUUID());
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);
            list.add(sysRoleMenuEntity);
        }
        this.insertBatch(list);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteBatch(String[] roleIds) {
        for (String roleId : roleIds) {
            this.deleteByMap(new MapUtils().put("role_id", roleId));
        }
        return 0;
    }
}
