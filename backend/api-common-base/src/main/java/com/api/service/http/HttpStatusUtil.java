package com.api.service.http;

import org.apache.http.HttpStatus;

public interface HttpStatusUtil extends HttpStatus {
    int SC_DATA_SOURCE = 600;//数据源未获取

    int SC_SIGN_IN = 601;//用户id,token未获取

    int SC_SERVER_REQUEST = 602;//服务器密码未识别

    int SC_PAY_NOTIFYFIND = 603;//支付回调解析失败

    int SC_UNKNOWN = 999;//系统未知异常

    int SC_AUTH_UNKNOWN = 604;//该功能需要管理员授权后才能使用

    int SC_MER_MOBILE_NOT_EXISTS = 605;//该手机号未注册，请先注册

    int SC_MER_MOBILE_EXISTS = 606;//该手机号已申请商户

    int SC_AUTH_LOGIN = 607;//该功能需要管理员授权后才能使用
}
