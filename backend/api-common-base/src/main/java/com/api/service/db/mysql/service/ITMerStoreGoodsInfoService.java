package com.api.service.db.mysql.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;


public interface ITMerStoreGoodsInfoService extends IService<TMerStoreGoodsInfoEntity> {


    void saveGoods(TMerStoreGoodsInfoEntity inserEntity);

    Page<TMerStoreGoodsInfoEntity> getStoreGoodsList(TMerStoreGoodsInfoEntity entity);

    Page<TMerStoreGoodsInfoEntity> getStoreGoodsList(TMerStoreGoodsInfoEntity entity, String[] platformGoodss);


    void convertEntityCos(TMerStoreGoodsInfoEntity entity);

    /**
     * 脱敏敏感数据
     *
     * @param entity
     */
    void desensitizationEntity(TMerStoreGoodsInfoEntity entity);
}
