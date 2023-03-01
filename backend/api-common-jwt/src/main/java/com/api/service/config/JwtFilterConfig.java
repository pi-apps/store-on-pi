package com.api.service.config;

import com.api.service.jwt.JwtAccessFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Filter配置
 */
@Configuration
public class JwtFilterConfig {

    @Bean
    public FilterRegistrationBean jwtAccessFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        JwtAccessFilter accessFilter = new JwtAccessFilter();
        registration.setFilter(accessFilter);
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("jwtAccessFilter");
        registration.setOrder(Integer.MAX_VALUE - 999);
        return registration;
    }
}
