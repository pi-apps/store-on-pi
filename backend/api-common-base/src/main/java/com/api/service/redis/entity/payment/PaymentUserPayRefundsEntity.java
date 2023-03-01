package com.api.service.redis.entity.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PaymentUserPayRefundsEntity implements Serializable {


    @ApiModelProperty(value = "退款订单表主键", dataType = "String")
    private String orderId;

    @ApiModelProperty(value = "退款订单ID", dataType = "String")
    private String refundOrderId;

    @ApiModelProperty(value = "收银台:支付id（支付宝订单id，微信订单id等）", dataType = "String")
    private String payOrderId;

    @ApiModelProperty(value = "业务线:指向业务表", dataType = "String")
    private String businessLineType;

    @ApiModelProperty(value = "业务类型: business_channel_type", dataType = "String")
    private String businessChannelType;

    @ApiModelProperty(value = "退款金额", dataType = "BigDecimal")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "必填:退款原因", dataType = "String")
    private String refundReason;


}
