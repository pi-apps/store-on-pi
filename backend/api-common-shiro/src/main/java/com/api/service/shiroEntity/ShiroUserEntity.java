package com.api.service.shiroEntity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
public class ShiroUserEntity<T> implements Serializable {

    private String userId;//用户id

    private String mobile;//用户手机号码

    private Set<String> permsSet;//用户权限标签

    private String status;//用户状态

    private String token;//用户令牌

    private Date expireTime;//用户令牌过期时间

    private T t;

}
