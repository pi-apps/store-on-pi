package com.api.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户下订单
 */
@Data
@ApiModel(value = "StoreGoodsOrderAddEntity")
public class StoreGoodsOrderAddEntity {

    @ApiModelProperty(value = "必填:门店id", dataType = "String")
    private String storeId;

    @ApiModelProperty(value = "必填:商品主键", dataType = "String")
    private String goodsId;

    @ApiModelProperty(value = "选填:经度", dataType = "String")
    private String longitude;

    @ApiModelProperty(value = "选填:纬度", dataType = "String")
    private String latitude;

    @ApiModelProperty(value = "下单备注", dataType = "String", notes = "下单备注")
    private String orderRemark;
}
