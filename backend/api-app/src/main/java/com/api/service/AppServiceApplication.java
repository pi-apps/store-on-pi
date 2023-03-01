package com.api.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@EnableEurekaClient//本服务启动后自动注册进Eureka服务中
@SpringBootApplication
public class AppServiceApplication {

    @PostConstruct
    void started() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
    }

    public static void main(String[] args) {
        SpringApplication.run(AppServiceApplication.class, args);
    }
}
