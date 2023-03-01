package com.api.service.db.mysql.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

public interface ITMerStoreServiceService extends IService<TMerStoreServiceEntity> {

    Page<TMerStoreServiceEntity> listService(TMerStoreServiceEntity entity);

    void addName(TMerStoreServiceEntity entity);

    void negate(String id);
}
