package com.api.service.login.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("CmsLoginEntity")
public class CmsLoginEntity implements Serializable {

    @ApiModelProperty(value = "手机号[必填]", dataType = "String")
    private String mobile;

    @ApiModelProperty(value = "密码[必填]", dataType = "String")
    private String password;



}
