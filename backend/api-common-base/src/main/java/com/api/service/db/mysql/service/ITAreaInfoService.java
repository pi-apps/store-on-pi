package com.api.service.db.mysql.service;

import com.api.service.db.entity.TAreaInfoEntity;
import com.api.service.sdk.baiduMap.BaiduMapEntity;
import com.baomidou.mybatisplus.service.IService;

public interface ITAreaInfoService extends IService<TAreaInfoEntity> {


    BaiduMapEntity getNameByBaidu(String lng, String lat);

    /**
     * 数据同步专用
     *
     * @param lng
     * @param lat
     * @return
     */
    BaiduMapEntity synGetNameByBaidu(String lng, String lat);
}
