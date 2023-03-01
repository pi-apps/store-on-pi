package com.api.service.db.entity;

import com.api.service.annotations.TxCosUrl;
import com.api.service.db.mysql.BaseMysqlTable;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@TableName(BaseMysqlTable.API_USER_TABLE)
@ApiModel(value = "TApiUserEntity", description = "数据库-主用户基础信息表")
public class TApiUserEntity extends AbstractPageEntity {

    private static final long serialVersionUID = 1L;


    @TableId("user_id")
    @ApiModelProperty(value = "主键", dataType = "String")
    private String userId;

    @TableField("user_name")
    @ApiModelProperty(value = "用户名", dataType = "String")
    private String userName;

    @TableField("access_token")
    @ApiModelProperty(value = "PI认证TOKEN", dataType = "String")
    private String accessToken;


    @TableField("status")
    @ApiModelProperty(value = "状态  0：正常   1：禁用", dataType = "String")
    private String status;

    @TableField("create_time")
    @ApiModelProperty(value = "创建时间", dataType = "Date", hidden = true)
    private Date createTime;

    @TableField("user_head_url")
    @ApiModelProperty(value = "头像", dataType = "String")
    @TxCosUrl
    private String userHeadUrl;

    @TableField("nick_name")
    @ApiModelProperty(value = "昵称", dataType = "String")
    private String nickName;

    @TableField("remarks")
    @ApiModelProperty(value = "备注", dataType = "String")
    private String remarks;

    @TableField("invitation")
    @ApiModelProperty(value = "邀请码", dataType = "String")
    private String invitation;

    @TableField("recommender_user_id")
    @ApiModelProperty(value = "推荐人id", dataType = "String")
    private String recommenderUserId;


    @TableField("is_store")
    @ApiModelProperty(value = "是否门店 0是1否", dataType = "String")
    private String isStore;

    @TableField("balance")
    @ApiModelProperty(value = "余额", dataType = "BigDecimal")
    private BigDecimal balance;

    @TableField("amount_signin_points")
    @ApiModelProperty(value = "个人签到积分(可用余额)", dataType = "BigDecimal")
    private BigDecimal amountSigninPoints;


    @TableField(value = "country_code", strategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "国家code", dataType = "String")
    private String countryCode;

    @TableField(value = "province_code", strategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "省代码", dataType = "String")
    private String provinceCode;

    @TableField(value = "city_code", strategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "市代码", dataType = "String")
    private String cityCode;

    @TableField(value = "district_code", strategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "区/县代码", dataType = "String")
    private String districtCode;


    @TableField("language")
    @ApiModelProperty(value = "用户语言", dataType = "String")
    private String language;
}
