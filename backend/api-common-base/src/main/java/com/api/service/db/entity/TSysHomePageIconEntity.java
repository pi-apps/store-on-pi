package com.api.service.db.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_sys_home_page_icon")
@ApiModel(value = "TSysFeedbackEntity", description = "数据库-首页图标配置相关"
)
public class TSysHomePageIconEntity extends AbstractPageEntity {
    @TableId("id")
    @ApiModelProperty(value = "主键", dataType = "String", notes = "主键")
    private String id;

    @TableField("title")
    @ApiModelProperty(value = "标题", dataType = "String", notes = "标题")
    private String title;

    @TableField("url")
    @ApiModelProperty(value = "图标", dataType = "String", notes = "图标")
    private String url;

    @TableField("isnew")
    @ApiModelProperty(value = "活动信息", dataType = "String", notes = "活动信息")
    private String isnew;


    @TableField("width")
    @ApiModelProperty(value = "宽度(25%)", dataType = "String", notes = "宽度(25%)")
    private String width;

    @TableField("system_type")
    @ApiModelProperty(value = "系统类型，0：Android 1：iOS 2：小程序", dataType = "String")
    private String systemType;

    @TableField("small_version_code")
    @ApiModelProperty(value = "最小版本号", dataType = "String")
    private Integer smallVersionCode;

    @TableField("big_version_code")
    @ApiModelProperty(value = "最大版本号", dataType = "String")
    private Integer bigVersionCode;


    @TableField("status")
    @ApiModelProperty(value = "是否展示 0：展示，1：不展示", dataType = "String", notes = "是否展示 0：展示，1：不展示")
    private String status;

    @TableField("level")
    @ApiModelProperty(value = "权重级别", dataType = "String", notes = "权重级别")
    private int level;


    @TableField("ad_url_type")
    @ApiModelProperty(value = "广告外链类型: ad_url", dataType = "String")
    private String adUrlType;

    @TableField("links_url")
    @ApiModelProperty(value = "链接路径", dataType = "String")
    private String linksUrl;


    @TableField("is_login")
    @ApiModelProperty(value = "是否需要登录.0是,1否,默认否", dataType = "String")
    private String isLogin;

    @TableField("gh_id")
    @ApiModelProperty(value = "微信原始id", dataType = "String")
    private String ghId;

    @TableField("wx_app_id")
    @ApiModelProperty(value = "微信ID", dataType = "String")
    private String wxAppId;


}
