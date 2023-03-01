package com.api.service.login.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "ApiMobileLoginEntity", description = "手机登录注册")
public class ApiMobileLoginEntity implements Serializable {

    @ApiModelProperty(value = "手机号码", dataType = "String")
    private String mobile;

    @ApiModelProperty(value = "密码", dataType = "String")
    private String password;

    @ApiModelProperty(value = "短信验证码", dataType = "String")
    private String msgCode;

    @ApiModelProperty(value = "邀请人邀请码", dataType = "String")
    private String recommenderInvitation;


}
