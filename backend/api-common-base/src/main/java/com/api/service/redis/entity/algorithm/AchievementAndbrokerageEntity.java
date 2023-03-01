package com.api.service.redis.entity.algorithm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AchievementAndbrokerageEntity implements Serializable {


    /**
     * 业绩类
     */
    @ApiModelProperty(value = "初始化必填:用户主键", dataType = "String")
    private String apiUserId;


    @ApiModelProperty(value = "初始化必填:用户父级线路ids(资金分配线路)", dataType = "String")
    private String pids;


    @ApiModelProperty(value = "初始化必填:业务线:指向业务表", dataType = "String")
    private String businessLineType;


    @ApiModelProperty(value = "初始化必填:业务类型: business_channel_type", dataType = "String")
    private String businessChannelType;


    @ApiModelProperty(value = "初始化必填:订单表主键", dataType = "String")
    private String orderId;


    @ApiModelProperty(value = "初始化必填:订单交易金额(真实付款金额)", dataType = "BigDecimal")
    private BigDecimal orderAmount;


    @ApiModelProperty(value = "初始化必填:可分配利润(资金)", dataType = "BigDecimal")
    private BigDecimal profit;


    /**
     * 佣金类
     */

    @ApiModelProperty(value = "初始化必填:资金解冻时间(资金释放时间,不到时间不允许分配)", dataType = "Date")
    private Date expireTime;

    @ApiModelProperty(value = "初始化必填:拓店者user_id", dataType = "String")
    private String storeDeveloperApiUserId;

    @ApiModelProperty(value = "初始化必填:订单归属区/县代码", dataType = "String")
    private String districtCode;

    @ApiModelProperty(value = "初始化必填:订单归属市代码", dataType = "String")
    private String cityCode;

    @ApiModelProperty(value = "初始化必填:订单归属省代码", dataType = "String")
    private String provinceCode;

    @ApiModelProperty(value = "初始化必填:订单归属大区代码", dataType = "String")
    private String regionCode;

    /**
     * 收银台类:用于第三步--分账
     */
    @ApiModelProperty(value = "必填:收银台:支付id（支付宝订单id，微信订单id等）", dataType = "String")
    private String payOrderId;

    @ApiModelProperty(value = "必填:收银台:服务费", dataType = "BigDecimal")
    private BigDecimal payCustomerFee;

    @ApiModelProperty(value = "必填:商品订单成本价(应付供应商/商户/门店成本金额)", dataType = "BigDecimal")
    private BigDecimal orderAmountCost;

    @ApiModelProperty(value = "商品提供方主键:供应商/商户/门店主键", dataType = "String")
    private String orderSupplierId;

    /**
     * 分账执行
     */
    @ApiModelProperty(value = "运算业绩、分佣算法后,是否立刻执行分账,[true立刻,fasle等待调用]默认等待调用", dataType = "String")
    private Boolean divideApply = false;
}
