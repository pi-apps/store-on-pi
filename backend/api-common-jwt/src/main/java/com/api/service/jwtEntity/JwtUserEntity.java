package com.api.service.jwtEntity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class JwtUserEntity implements Serializable {

    private String userId;//用户id

    private Date expireTime;//用户令牌过期时间


}
