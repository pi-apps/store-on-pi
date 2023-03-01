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
@TableName("t_sign_log")
@ApiModel("TSignLogEntity")
public class TSignLogEntity extends AbstractPageEntity {

    @TableId("sign_id")
    @ApiModelProperty(value = "主键", dataType = "String", notes = "主键")
    private String signId;

    @TableField("user_id")
    @ApiModelProperty(value = "用户id", dataType = "String", notes = "用户id")
    private String userId;

    @TableField("sign_date")
    @ApiModelProperty(value = "签到日期 YYYY-MM-DD", dataType = "String", notes = "签到日期 YYYY-MM-DD")
    private String signDate;

    @TableField("config_id")
    @ApiModelProperty(value = "命中签到配置ID", dataType = "String", notes = "命中签到配置ID")
    private String configId;

    @TableField("score_num")
    @ApiModelProperty(value = "积分数量", dataType = "Integer", notes = "积分数量")
    private BigDecimal scoreNum;

    @TableField("sign_days")
    @ApiModelProperty(value = "连续签到天数", dataType = "Integer", notes = "连续签到天数")
    private Integer signDays;

    @TableField("is_after_sign")
    @ApiModelProperty(value = "是否补签 0是 1否", dataType = "String", notes = "是否补签 0是 1否")
    private String isAfterSign;

    @TableField("create_time")
    @ApiModelProperty(value = "实际签到日期/使用积分日期", dataType = "String", notes = "实际签到日期/使用积分日期")
    private Date createTime;

    @TableField("account_trend")
    @ApiModelProperty(value = "积分方向，0收入，1支出", dataType = "String")
    private String accountTrend;

    @TableField("order_id")
    @ApiModelProperty(value = "业务订单ID", dataType = "String", notes = "业务订单ID")
    private String orderId;

    @TableField("score_type")
    @ApiModelProperty(value = "积分类型 0:签到领取积分 1:商城下单扣除积分,2看广告获得积分，3 转换车宝积分 ,更多类型按需填写")
    private String scoreType;


}
