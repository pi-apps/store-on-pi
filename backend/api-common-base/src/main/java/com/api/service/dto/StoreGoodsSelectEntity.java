package com.api.service.dto;

import com.api.service.db.entity.AbstractPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品查询
 */
@Data
@ApiModel(value = "StoreGoodsSelectEntity")
public class StoreGoodsSelectEntity extends AbstractPageEntity {

    @ApiModelProperty(value = "商品分类ID", dataType = "String")
    private String goodsServiceId;

    @ApiModelProperty(value = "选填:商品名称", dataType = "String")
    private String goodsName;

    @ApiModelProperty(value = "选填:商品上下架状态 0上架,1下架", dataType = "String")
    private String goodsStatus;


}
