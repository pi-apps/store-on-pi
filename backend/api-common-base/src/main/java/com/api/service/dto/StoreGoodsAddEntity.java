package com.api.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品添加
 */

@Data
@ApiModel(value = "StoreGoodsAddEntity")
public class StoreGoodsAddEntity {

    @ApiModelProperty(value = "必填:商品分类ID", dataType = "String")
    private String goodsServiceId;

    @ApiModelProperty(value = "必填:商品名称", dataType = "String")
    private String goodsName;

    @ApiModelProperty(value = "必填:商品小文字简介", dataType = "String")
    private String goodsDescribe;

    @ApiModelProperty(value = "必填:商品服务项目介绍", dataType = "String")
    private String goodsServiceDescribe;

    @ApiModelProperty(value = "必填:商品轮播图", dataType = "List<String>")
    private List<String> goodsBannerUrlList;

    @ApiModelProperty(value = "选填:商品首图小视频", dataType = "String")
    private String goodsBannerVideoUrl;

    @ApiModelProperty(value = "必填:商品详情图片介绍(流量太大了,已弃用)", dataType = "String")
    private List<String> goodsDetailsPhotosUrllList;

    @ApiModelProperty(value = "商品详情文字介绍", dataType = "String")
    private String goodsDetails;

    @ApiModelProperty(value = "必填:商品上下架状态 0上架,1下架", dataType = "String")
    private String goodsStatus;

    @ApiModelProperty(value = "必填:商品挂牌价(划线价)", dataType = "BigDecimal")
    private BigDecimal listingPrice;

    @ApiModelProperty(value = "二选一可组合:用户成交价", dataType = "BigDecimal")
    private BigDecimal userFinalPrice;

    @ApiModelProperty(value = "二选一可组合:到店支付价格", dataType = "BigDecimal")
    private BigDecimal arrivalAmount;

    @ApiModelProperty(value = "库存", dataType = "int")
    private int inventory;


    @ApiModelProperty(value = "选填:优先级越小越靠前,最大999", dataType = "int")
    private int level;


}
