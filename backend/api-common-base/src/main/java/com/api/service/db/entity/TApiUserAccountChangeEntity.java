package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@TableName("t_api_user_account_change")
@ApiModel(value = "TApiUserAccountChangeEntity", description = "数据库-用户订单账目变动表")
public class TApiUserAccountChangeEntity extends AbstractPageEntity {

    @TableId("id")
    @ApiModelProperty(value = "主键", dataType = "String")
    private String id;

    @TableField("user_name")
    @ApiModelProperty(value = "用户ID", dataType = "String")
    private String userName;

    @TableField("amount")
    @ApiModelProperty(value = "变动金额", dataType = "String")
    private BigDecimal amount;

    @TableField("remark")
    @ApiModelProperty(value = "变动原因", dataType = "String")
    private String remark;

    @TableField("order_id")
    @ApiModelProperty(value = "订单号", dataType = "String")
    private String orderId;

    @TableField("create_time")
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private Date createTime;

}
