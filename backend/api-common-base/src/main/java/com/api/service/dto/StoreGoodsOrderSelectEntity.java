package com.api.service.dto;

import com.api.service.db.entity.AbstractPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户订单列表
 */
@Data
@ApiModel(value = "StoreGoodsOrderSelectEntity")
public class StoreGoodsOrderSelectEntity extends AbstractPageEntity {

    @ApiModelProperty(value = "选填:商品名称", dataType = "String")
    private String goodsName;


    @ApiModelProperty(value = "选填:核销:是否使用 0已使用,1未使用 2已取消", dataType = "String")
    private String isUse;

    @ApiModelProperty(value = "收银台:支付状态 默认[10007001待支付]", dataType = "String")
    private String payStatusType;

}
