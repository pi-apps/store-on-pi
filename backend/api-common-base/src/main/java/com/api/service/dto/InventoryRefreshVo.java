package com.api.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Author: fengjian
 * Date: 2020/11/14
 * Time: 13:24
 * Description:
 */
@Data
@ApiModel(value = "InventoryRefreshVo", description = "商城系统——商品批量更新SKU库存Vo")
public class InventoryRefreshVo implements Serializable {

    @ApiModelProperty(value = "秒杀SKU ID", dataType = "String", notes = "秒杀SKU ID")
    private String seckillSkuId;

    @ApiModelProperty(value = "SKU ID", dataType = "String", notes = "SKU ID")
    private String skuId;

    @ApiModelProperty(value = "SKU名称", dataType = "String", notes = "SKU名称")
    private String skuName;

    @ApiModelProperty(value = "当前库存", dataType = "Integer", notes = "当前库存")
    private Integer currentInventory;

    @ApiModelProperty(value = "调整库存数量", dataType = "Integer", notes = "调整库存数量")
    private Integer inventory;

}