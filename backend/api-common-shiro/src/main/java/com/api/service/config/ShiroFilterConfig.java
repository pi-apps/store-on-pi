package com.api.service.config;

import com.api.service.oauth2.ShiroAccessFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * Filter配置
 */
@Configuration
public class ShiroFilterConfig {

    @Bean
    public FilterRegistrationBean shiroAccessFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        ShiroAccessFilter accessFilter = new ShiroAccessFilter();
        registration.setFilter(accessFilter);
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("shiroAccessFilter");
        registration.setOrder(Integer.MAX_VALUE - 999);
        return registration;
    }

    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setEnabled(true);
        registration.setOrder(Integer.MAX_VALUE - 1);
        registration.addUrlPatterns("/*");
        return registration;
    }


}
