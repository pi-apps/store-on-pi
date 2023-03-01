package com.api.service.db.mysql.service;

import com.baomidou.mybatisplus.service.IService;

import java.util.List;


public interface ITCMSRoleMenuService extends IService<TCmsRoleMenuEntity> {

    List<TCmsMenuEntity> queryMenuList(String roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(String[] roleIds);

    void saveOrUpdate(String roleId, List<String> roleMenuIdList);

}
