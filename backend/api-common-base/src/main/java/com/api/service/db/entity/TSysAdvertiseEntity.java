package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 广告
 */
@Data
@TableName("t_sys_advertise")
@ApiModel("TSysAdvertiseEntity")
public class TSysAdvertiseEntity extends AbstractPageEntity {

    private static final long serialVersionUID = 1L;

    @TableId("av_id")
    @ApiModelProperty(value = "主键", dataType = "String")
    private String avId;

    @TableField("ad_img_url")
    @ApiModelProperty(value = "广告图片URL", dataType = "String")
    private String adImgUrl;

    @TableField("ad_img_name")
    @ApiModelProperty(value = "广告图片名称", dataType = "String")
    private String adImgName;

    @TableField("status")
    @ApiModelProperty(value = "状态,0正常,1禁用", dataType = "String")
    private String status;

    @TableField("level")
    @ApiModelProperty(value = "权重", dataType = "Integer")
    private Integer level;

    @TableField("create_time")
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private Date createTime;

    @TableField("publish_time")
    @ApiModelProperty(value = "上架时间", dataType = "Date")
    private Date publishTime;

    @TableField("recall_time")
    @ApiModelProperty(value = "下架时间", dataType = "Date")
    private Date recallTime;

    @TableField("ad_site_type")
    @ApiModelProperty(value = "广告位置类型: ad_site", dataType = "String")
    private String adSiteType;

    @TableField("ad_url_type")
    @ApiModelProperty(value = "广告外链类型: ad_url", dataType = "String")
    private String adUrlType;

    @TableField("links_url")
    @ApiModelProperty(value = "链接路径", dataType = "String")
    private String linksUrl;

    @TableField("gh_id")
    @ApiModelProperty(value = "微信原始id", dataType = "String")
    private String ghId;

    @TableField("wx_app_id")
    @ApiModelProperty(value = "微信ID", dataType = "String")
    private String wxAppId;

    @TableField("language")
    @ApiModelProperty(value = "语言 0中文 1英文", dataType = "String")
    private String language;
}


