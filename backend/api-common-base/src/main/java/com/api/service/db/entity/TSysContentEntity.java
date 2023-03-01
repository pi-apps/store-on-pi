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
 * 文章
 */
@Data
@TableName("t_sys_content")
@ApiModel("TSysContentEntity")
public class TSysContentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @ApiModelProperty(value = "主键", dataType = "String")
    private String id;

    @TableField("name")
    @ApiModelProperty(value = "标题名字", dataType = "String")
    private String name;

    @TableField("context")
    @ApiModelProperty(value = "内容", dataType = "String")
    private String context;

    @TableField("status")
    @ApiModelProperty(value = "使用状态 0正常,1禁用", dataType = "String")
    private String status;

    @TableField("create_time")
    @ApiModelProperty(value = "更新时间", dataType = "Date")
    private Date createTime;


}
