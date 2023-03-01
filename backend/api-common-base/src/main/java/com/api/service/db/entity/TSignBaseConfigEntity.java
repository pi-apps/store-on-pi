package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_sign_base_config")
@ApiModel(value = "TSignBaseConfigEntity", description = "数据库-连续签到基础配置表")
public class TSignBaseConfigEntity extends AbstractPageEntity {

    @TableId("id")
    @ApiModelProperty(value = "主键", dataType = "String", notes = "主键")
    private String id;

    @TableField("sign_base_score_num")
    @ApiModelProperty(value = "每日基础签到积分", dataType = "BigDecimal")
    private BigDecimal signBaseScoreNum;

    @TableField("amount_points")
    @ApiModelProperty(value = "补签价格(车宝积分)", dataType = "BigDecimal")
    private BigDecimal amountPoints;

    @TableField("sign_base_day")
    @ApiModelProperty(value = "连续签到周期", dataType = "Integer")
    private Integer signBaseDay;

    @TableField("status")
    @ApiModelProperty(value = "0启用1 禁用", dataType = "String")
    private String status;

    @TableField("remark")
    @ApiModelProperty(value = "签到说明", dataType = "String")
    private String remark;

}