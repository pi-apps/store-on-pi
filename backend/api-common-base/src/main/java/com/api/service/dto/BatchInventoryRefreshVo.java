package com.api.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: fengjian
 * Date: 2020/11/14
 * Time: 13:24
 * Description:
 */
@Data
@ApiModel(value = "BatchInventoryRefreshVo", description = "商城系统——商品批量更新库存Vo")
public class BatchInventoryRefreshVo implements Serializable {

    @ApiModelProperty(value = "秒杀ID", dataType = "String", notes = "秒杀ID")
    private String seckillId;

    @ApiModelProperty(value = "总库存(总量)", dataType = "Integer", notes = "总库存(总量)")
    private Integer totalInventory;

    @ApiModelProperty(value = "当前剩余库存", dataType = "Integer", notes = "当前剩余库存")
    private Integer currentTotalInventory;

    @ApiModelProperty(value = "库存信息", dataType = "List", notes = "库存信息")
    private List<InventoryRefreshVo> inventoryList;

}