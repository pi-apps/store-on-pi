package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_sign_date_config")
@ApiModel(value = "TSignDateConfigEntity", description = "数据库-连续签到天数积分配置表")
public class TSignDateConfigEntity extends AbstractPageEntity {

    @TableId("config_id")
    @ApiModelProperty(value = "主键", dataType = "String", notes = "主键")
    private String configId;

    @TableField("score_num")
    @ApiModelProperty(value = "额外获得积分数量", dataType = "Integer", notes = "额外获得积分数量")
    private BigDecimal scoreNum;

    @TableField("sign_days")
    @ApiModelProperty(value = "连续签到天数", dataType = "Integer", notes = "连续签到天数")
    private Integer signDays;

    @TableField("status")
    @ApiModelProperty(value = "0启用1 禁用", dataType = "String", notes = "0启用1 禁用")
    private String status;

}