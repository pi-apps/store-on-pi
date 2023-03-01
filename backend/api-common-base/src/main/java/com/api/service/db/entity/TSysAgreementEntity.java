package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 协议书
 */
@Data
@TableName("t_sys_agreement")
@ApiModel("TSysAgreementEntity")
public class TSysAgreementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("agreement_id")
    @ApiModelProperty(value = "主键", dataType = "String")
    private String agreementId;

    @TableField("name")
    @ApiModelProperty(value = "协议书名字", dataType = "String")
    private String name;

    @TableField("agreement_context")
    @ApiModelProperty(value = "协议书文本html", dataType = "String")
    private String agreementContext;

    @TableField("status")
    @ApiModelProperty(value = "使用状态 0正常,1禁用", dataType = "String")
    private String status;

    @TableField("update_time")
    @ApiModelProperty(value = "更新时间", dataType = "Date")
    private Date updateTime;



}
