package com.api.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 门店审核
 */
@Data
@ApiModel(value = "StoreAddOrUpdateEntity")
public class StoreAddOrUpdateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "修改必填:主键", dataType = "String", position = 1)
    private String storeId;
    /**
     * 推荐人信息
     */
    @ApiModelProperty(value = "选填:邀请人PI用户名", dataType = "String", position = 2)
    private String inviterApiUserName;

    @ApiModelProperty(value = "登录id", dataType = "String")
    private String cmsUserId;

    @ApiModelProperty(value = "门店分类(码表)", dataType = "String")
    private String storeType;


    /**
     * 主表信息
     */
    @ApiModelProperty(value = "1.基础信息:必填:门店名称", dataType = "String", position = 3)
    private String storeName;

    @ApiModelProperty(value = "公司简称", dataType = "String")
    private String shortName;

    @ApiModelProperty(value = "2.基础信息:必填:经度", dataType = "String", position = 4)
    private String longitude;

    @ApiModelProperty(value = "3.基础信息:必填:纬度", dataType = "String", position = 5)
    private String latitude;


    @ApiModelProperty(value = "国家code", dataType = "String")
    private String countryCode;

    @ApiModelProperty(value = "省代码", dataType = "String")
    private String provinceCode;


    @ApiModelProperty(value = "市代码", dataType = "String")
    private String cityCode;


    @ApiModelProperty(value = "区/县代码", dataType = "String")
    private String districtCode;


    @ApiModelProperty(value = "4.基础信息:必填:门店详细地址", dataType = "String", position = 6)
    private String address;

    @ApiModelProperty(value = "5.基础信息:必填:门店联系电话", dataType = "String", position = 7)
    private String mobile;

    @ApiModelProperty(value = "6.基础信息:必填:门店LOGO", dataType = "String", position = 8)
    private String doorPhotoUrl;

    @ApiModelProperty(value = "7.基础信息:选填:门店小视频", dataType = "String", position = 9)
    private String doorPhotoVideoUrl;

    @ApiModelProperty(value = "8.基础信息:图片集合", dataType = "String", position = 10)
    private List<String> photosUrlList;

    @ApiModelProperty(value = "13.基础信息:营业开始时间  格式：时分", dataType = "String", position = 15)
    private String openTimeStart;

    @ApiModelProperty(value = "14.基础信息:营业结束时间  格式：时分", dataType = "String", position = 16)
    private String openTimeEnd;
}