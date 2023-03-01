package com.api.service.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 配置类
 */
@Configuration
public class RedissonConfig {

    @Value("${redis-data.cluster.nodes}")
    private String host;

    @Value("${redis-data.password}")
    private String password;

    @Value("${redis-data.max-active}")
    private int poolSize;

    @Value("${redis-data.min-idle}")
    private int minIdle;

    @Bean
    public RedissonClient getRedisson() {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host).setPassword(password)
                .setConnectionPoolSize(poolSize)
                .setConnectionMinimumIdleSize(minIdle)
                .setConnectTimeout(5000).setTimeout(5000);

        return Redisson.create(config);
    }

}
