package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@TableName("t_pay_order_log")
@ApiModel(value = "TPayOrderLogEntity", description = "数据库-支付订单发起记录表")
public class TPayOrderLogEntity extends AbstractPageEntity {

    private static final long serialVersionUID = 1L;


    @TableId("order_id")
    @ApiModelProperty(value = "主键", dataType = "String")
    private String orderId;

    @TableField("business_line_type")
    @ApiModelProperty(value = "业务线", dataType = "String")
    private String businessLineType;

    @TableField("business_channel_type")
    @ApiModelProperty(value = "业务渠道", dataType = "String")
    private String businessChannelType;

    @TableField("create_time_str")
    @ApiModelProperty(value = "发起时间 yyyyMMdd", dataType = "String")
    private String createTimeStr;

    @TableField("pay_channel_name")
    @ApiModelProperty(value = "收银台名称 : 默认易宝", dataType = "String")
    private String payChannelName;


    @TableField("create_time")
    @ApiModelProperty(value = "创建时间", dataType = "Date", hidden = true)
    private Date createTime;

    @TableField("pay_channel_type")
    @ApiModelProperty(value = "支付渠道", dataType = "String")//1
    private String payChannelType;

    @TableField("pay_text")
    @ApiModelProperty(value = "支付报文", dataType = "String")//1
    private String payText;


}
