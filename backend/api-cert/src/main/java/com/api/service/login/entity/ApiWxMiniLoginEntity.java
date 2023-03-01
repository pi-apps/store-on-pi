package com.api.service.login.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "ApiWxMiniLoginEntity", description = "小程序登录注册")
public class ApiWxMiniLoginEntity implements Serializable {

    @ApiModelProperty(value = "微信页面code", dataType = "String", notes = "必填")
    private String code;

    @ApiModelProperty(value = "密文", dataType = "String", notes = "必填")
    private String encryptData;

    @ApiModelProperty(value = "偏差值", dataType = "String", notes = "必填")
    private String iv;

    @ApiModelProperty(value = "邀请人邀请码", dataType = "String", notes = "邀请人邀请码")
    private String recommenderInvitation;


}
