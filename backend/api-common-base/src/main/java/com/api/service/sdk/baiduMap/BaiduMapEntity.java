package com.api.service.sdk.baiduMap;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "BaiduMapEntity", description = "百度地图实体对象")
public class BaiduMapEntity implements Serializable {

    @ApiModelProperty(value = "经度", dataType = "String")
    private String lng;

    @ApiModelProperty(value = "纬度", dataType = "String")
    private String lat;

    @ApiModelProperty(value = "大区名称", dataType = "String")
    private String regionName;

    @ApiModelProperty(value = "大区代码", dataType = "String")
    private String regionCode;


    @ApiModelProperty(value = "省名称", dataType = "String")
    private String provinceName;

    @ApiModelProperty(value = "省代码", dataType = "String")
    private String provinceCode;

    @ApiModelProperty(value = "市名称", dataType = "String")
    private String cityName;

    @ApiModelProperty(value = "市代码", dataType = "String")
    private String cityCode;

    @ApiModelProperty(value = "区/县名称", dataType = "String")
    private String districtName;

    @ApiModelProperty(value = "区/县代码", dataType = "String")
    private String districtCode;

    @ApiModelProperty(value = "具体位置", dataType = "String")
    private String locationName;

}
