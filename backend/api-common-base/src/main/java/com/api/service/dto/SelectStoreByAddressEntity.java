package com.api.service.dto;

import com.api.service.db.entity.AbstractPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "SelectStoreByAddressEntity")
public class SelectStoreByAddressEntity extends AbstractPageEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "国家", dataType = "String")
    private String countryCode;

    @ApiModelProperty(value = "省", dataType = "String")
    private String provinceCode;

    @ApiModelProperty(value = "市", dataType = "String")
    private String cityCode;

    @ApiModelProperty(value = "区", dataType = "String")
    private String districtCode;

    @ApiModelProperty(value = "店铺名称", dataType = "String")
    private String storeName;

    @ApiModelProperty(value = "门店分类", dataType = "String")
    private String storeType;


}