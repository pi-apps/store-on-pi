package com.api.service.db.mysql.service;

import com.baomidou.mybatisplus.service.IService;


public interface ITMerStoreBuyOrderService extends IService<TMerStoreBuyOrderEntity> {


    TMerStoreBuyOrderEntity selectByThirdId(String thirdId);

}
