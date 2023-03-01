package com.api.service.db.mysql.service;

import com.api.service.db.entity.TPayOrderLogEntity;
import com.baomidou.mybatisplus.service.IService;


public interface ITPayOrderLogService extends IService<TPayOrderLogEntity> {

    /**
     * @param orderId             订单号
     * @param businessLineType    业务线
     * @param businessChannelType 业务渠道
     * @param payChannelName      收银台 默认10013002 易宝支付
     * @param payText             支付报文
     * @param payChannelType      支付方式
     */
    void inserEntity(String orderId, String businessLineType, String businessChannelType, String payChannelName, String payText, String payChannelType);

}
