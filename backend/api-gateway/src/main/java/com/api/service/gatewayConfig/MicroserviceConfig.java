package com.api.service.gatewayConfig;

/**
 * 微服务配置
 */
public class MicroserviceConfig {

    /**
     * # 路由所接管的微服务 微服务的application name名字 必须带上 "-service"
     * # 如cert服务,名字叫  cert-service
     * # 如oilStation服务,名字叫 oilStation-service
     *
     * @param builder
     * @return
     */
    /**
     * 用于配置微服务
     */
    public static final String[] services = new String[]{
            "cert" //认证服务
            , "cms"//内管服务
            , "app"//api用户基础数据服务
            , "mer"//商家服务
    };
}
