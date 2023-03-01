package com.api.service.db.mysql.service;

import com.baomidou.mybatisplus.service.IService;

import java.util.List;


public interface ITCMSUserRoleService extends IService<TCmsUserRoleEntity> {
    List<TCmsRoleEntity> queryAllRole(String userId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(String[] roleIds);

    List<String> queryRoleIdList(String createUserId);

    void saveOrUpdate(String userId, List<String> roleIdList);
}
