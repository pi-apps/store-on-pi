package com.api.service.jwt;


import com.api.service.JwtThreadLocalMap;
import com.api.service.db.entity.TApiUserEntity;

public class JwtController {

    public TApiUserEntity getUser() {
        return JwtThreadLocalMap.getUser();
    }

    public String getUserId() {
        return getUser().getUserId();
    }

    public String success() {
        return "处理成功";
    }

    public String fail(Exception e) {
        if (e instanceof RuntimeException) {
            RuntimeException exception = (RuntimeException) e;
            return "处理失败:" + exception.getMessage();
        }
        return "处理失败:" + e.getMessage();
    }
}
