package com.api.service.config;

import com.api.service.RedisUtil;
import com.api.service.oauth2.OAuth2Filter;
import com.api.service.oauth2.OAuth2Realm;
import com.api.service.shiroDao.RedisSessionDao;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Shiro配置
 */
@Configuration
public class ShiroConfig {
    public static final long expireTime = 3600 * 24 * 15L;

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, @Value("${spring.application.name}") String applicationName) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);
        Map<String, String> filterMap = new LinkedHashMap<>();
        /**
         * 开放swagger相关调用
         */
        filterMap.put("/swagger-resources/**", "anon");//输出
        filterMap.put("/v2/api-docs", "anon");//文档
        filterMap.put("/v2/api-docs-ext", "anon");//文档
        filterMap.put("/doc.html", "anon");
        filterMap.put("/webjars/**", "anon");
        /**
         * 微服务相关
         */
        filterMap.put("/cert/**", "anon");//认证服务不进行鉴权
        filterMap.put("/unAuth/**", "anon");//关键字不鉴权
        /**
         * 监控不鉴权
         */
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/druid/**", "anon");//开放sql监控



        /**
         * 开启过滤拦截
         */
        filterMap.put("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    /**
     * Shiro生命周期处理器
     * 此方法需要用static作为修饰词，否则无法通过@Value()注解的方式获取配置文件的值
     */
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /***
     * 授权所用配置
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /***
     * 使授权注解起作用不如不想配置可以在pom文件中加入
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    //会话
    @Bean
    public SessionManager sessionManager(RedisUtil redisUtil) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDao(redisUtil));
        sessionManager.setGlobalSessionTimeout(expireTime);
        return sessionManager;
    }

    //验证
    @Bean
    public DefaultWebSecurityManager securityManager(@Qualifier("oAuth2Realm") OAuth2Realm oAuth2Realm, RedisUtil redisUtil) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager(redisUtil));
        return securityManager;
    }

    @Bean
    public RedisSessionDao redisSessionDao(RedisUtil redisUtile) {
        return new RedisSessionDao(redisUtile.redisTemplate());
    }

}
