package com.api.service.unAuth.access.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "PaymentDto", description = "支付对象")
public class PaymentDto implements Serializable {

    @ApiModelProperty(value = "我方订单号", dataType = "String")
    private String orderId;

    @ApiModelProperty(value = "PI支付ID", dataType = "String")
    private String paymentId;


}
