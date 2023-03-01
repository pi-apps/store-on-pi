package com.api.service.unAuth.access.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "CompleteDto", description = "支付完成对象")
public class CompleteDto implements Serializable {


    @ApiModelProperty(value = "PI支付ID", dataType = "String")
    private String paymentId;

    @ApiModelProperty(value = "txId", dataType = "String")
    private String txId;

    @ApiModelProperty(value = "订单ID【余额支付参数】", dataType = "String")
    private String orderId;

    @ApiModelProperty(value = "支付方式：0：PI钱包 1：余额支付", dataType = "String")
    private String payType;


}
