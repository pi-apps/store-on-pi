package com.api.service.unAuth.access.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "LoginDto", description = "前端调SDK获取的用户信息")
public class LoginDto implements Serializable {

    @ApiModelProperty(value = "用户名", dataType = "String")
    private String userName;

    @ApiModelProperty(value = "用户ID", dataType = "String")
    private String uid;

    @ApiModelProperty(value = "角色", dataType = "String")
    private String roles;

    @ApiModelProperty(value = "token", dataType = "String")
    private String accessToken;

}
