package com.api.service.dto;

import com.api.service.db.entity.AbstractPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 门店订单
 */
@Data
@ApiModel(value = "StoreGoodsOrderInfoEntity")
public class StoreGoodsOrderInfoEntity extends AbstractPageEntity {


    @ApiModelProperty(value = "选填:商品名称", dataType = "String")
    private String goodsName;

    @ApiModelProperty(value = "必填核销:是否使用 0已使用,1未使用 2已取消", dataType = "String")
    private String isUse;

    @ApiModelProperty(value = "选填:用户ID", dataType = "String")
    private String apiUserId;

    @ApiModelProperty(value = "选填:用户名", dataType = "String")
    private String apiUserName;



}
