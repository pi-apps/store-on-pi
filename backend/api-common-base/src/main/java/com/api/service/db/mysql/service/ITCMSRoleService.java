package com.api.service.db.mysql.service;

import com.api.service.db.mysql.util.PageUtils;
import com.baomidou.mybatisplus.service.IService;


public interface ITCMSRoleService extends IService<TCmsRoleEntity> {

    PageUtils queryPage(TCmsRoleEntity entity);

    void save(TCmsRoleEntity entity);

    void update(TCmsRoleEntity entity);

    void delete(String[] roleIds);
}

