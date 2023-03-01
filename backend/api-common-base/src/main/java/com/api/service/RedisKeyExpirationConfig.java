package com.api.service;


/**
 * redis key过期监听常量
 */
public class RedisKeyExpirationConfig {


    //收银订单退款
    public static final String PAYMENT_PAY_REFUND_ORDER = "payment:pay:refund:order-";
    public static final String PAYMENT_PAY_REFUND_ORDER_ENTITY = "payment:pay:refund:order:entity:";//监听具体数据


    /**
     * 订单过期
     */
    public static final String MER_STORE_ORDER_ID = "mer:order:id-";


}
