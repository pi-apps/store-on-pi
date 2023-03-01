package com.api.service.dto;

import java.math.BigDecimal;

public class Constant {

    public static BigDecimal settlementRate = new BigDecimal("0.92");//门店结算比例

    public static BigDecimal inviterSettlementRate = new BigDecimal("0.6");//利润的60%给推广者

    public static BigDecimal inviterUserRate = new BigDecimal("0.2");//利润的20%给用户直推

}