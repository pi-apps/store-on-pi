package com.api.service.unAuth.access.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "IncompleteDto", description = "失败订单对象")
public class IncompleteDto implements Serializable {

    @ApiModelProperty(value = "identifier", dataType = "String")
    private String identifier;

    @ApiModelProperty(value = "transaction", dataType = "TransactionDto")
    private TransactionDto transaction;


}
