package com.api.service.db.mysql.service;

import com.baomidou.mybatisplus.service.IService;


public interface ITMerStoreGoodsOrderService extends IService<TMerStoreGoodsOrderEntity> {

    /**
     * @param entity
     */
    void convertEntityCos(TMerStoreInfoEntity entity);

    /**
     * 脱敏敏感数据
     *
     * @param entity
     */
    void desensitizationEntity(TMerStoreGoodsOrderEntity entity);

    void refund(TMerStoreGoodsOrderEntity tMerStoreGoodsOrderEntity);

    /**
     * 余额支付
     * @param tMerStoreGoodsOrderEntity
     */
    void pay(TMerStoreGoodsOrderEntity tMerStoreGoodsOrderEntity);
}
