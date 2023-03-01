package com.api.service.unAuth.access.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TransactionDto", description = "前端失败订单详情")
public class TransactionDto {

    @ApiModelProperty(value = "txid", dataType = "String")
    private String txid;

    @ApiModelProperty(value = "_link", dataType = "String")
    private String _link;
}
