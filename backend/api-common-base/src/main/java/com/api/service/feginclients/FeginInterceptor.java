package com.api.service.feginclients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;


/**
 * @author Ashy.Cheung
 * @remarks 服务发出请求前最后的拦截校验
 * @DATE 2020.07.04
 */
@Configuration
public class FeginInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("fegin", "true");
    }
}
