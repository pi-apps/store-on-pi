package com.api.service.db.mysql.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;


public interface ITMerStoreInfoService extends IService<TMerStoreInfoEntity> {


    /**
     * 查询所有门店
     *
     * @param entity
     * @return
     */
    Page<TMerStoreInfoEntity> getStoreList(TMerStoreInfoEntity entity);


    /**
     * 搜索附近门店
     * 必填:经纬度
     * 选填:类型
     * 选填:范围 默认3000米
     *
     * @param entity
     * @return
     */
    Page<TMerStoreInfoEntity> getPage(TMerStoreInfoEntity entity);


    /**
     * 快照图片转换
     *
     * @param entity
     */
    void convertEntityCos(TMerStoreInfoEntity entity);

    /**
     * 脱敏敏感数据
     *
     * @param entity
     */
    void desensitizationEntity(TMerStoreInfoEntity entity);


}
