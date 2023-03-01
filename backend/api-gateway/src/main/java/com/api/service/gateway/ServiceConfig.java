package com.api.service.gateway;

import com.api.service.gatewayConfig.MicroserviceConfig;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;


/**
 * 统一路由器,此处非性能问题,不需要进行修改
 *
 * @author Ashy.cheung
 * @date 2020.12.03
 */
@Configuration
public class ServiceConfig {


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        for (String service : MicroserviceConfig.services) {
            routes.route(r -> r.path("/" + service + "/**")
                    .filters(f -> f
                            .hystrix(config -> config
                                    .setFallbackUri("forward:/sys/fallback?service=" + service)) //配置统一断路回调
                            .requestRateLimiter(config -> { //配置限流
                                config.setRateLimiter(redisRateLimiter());
                                config.setKeyResolver(ipKeyResolver());
                            })
                    )
                    .uri("lb://" + service + "-service")).build();
        }
        return routes.build();
    }

    /**
     * IP限流方案
     *
     * @return
     */
    @Bean("ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    /**
     * 分布式限流令牌桶方式
     *
     * @return
     */
    @Bean("redisRateLimiter")
    public RedisRateLimiter redisRateLimiter() {
        /**
         * replenishRate：允许用户每秒处理多少个请求
         * burstCapacity：令牌桶的容量，允许在一秒钟内完成的最大请求数
         */
        return new RedisRateLimiter(1500, 2500);
    }

}
