package com.api.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品更新
 */
@Data
@ApiModel(value = "StoreGoodsUpdateEntity")
public class StoreGoodsUpdateEntity {

    @ApiModelProperty(value = "必填:商品主键", dataType = "String")
    private String id;

    @ApiModelProperty(value = "商品名称", dataType = "String")
    private String goodsName;

    @ApiModelProperty(value = "商品logo图", dataType = "String")
    private String goodsBannerUrl;

    @ApiModelProperty(value = "商品上下架状态 0上架,1下架", dataType = "String")
    private String goodsStatus;

    @ApiModelProperty(value = "用户支付PI价格", dataType = "BigDecimal")
    private BigDecimal userFinalPrice;

    @ApiModelProperty(value = "到店支付", dataType = "BigDecimal")
    private BigDecimal arrivalAmount;

    @ApiModelProperty(value = "商品详情文字介绍", dataType = "String")
    private String goodsDetails;


}
