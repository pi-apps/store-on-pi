
package com.api.service.config;

import com.api.service.serializer.RedisObjectSerializer;
import com.api.service.RedisLockUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * redis
 *
 * @author Ashy.Cheung
 * @Date 2020.07.03
 */
@Component
public class RedisConfig {
    @Value("${redis-data.is-cluster}")
    private boolean isCluster;
    @Value("${redis-data.cluster.nodes}")
    private String nodes; //节点
    @Value("${redis-data.password}")
    private String password; //密码
    @Value("${redis-data.max-active}")
    private Integer maxTotal; //同一时刻可分配最大连接数
    @Value("${redis-data.max-idle}")
    private Integer maxIdle; //最大空闲连接
    @Value("${redis-data.min-idle}")
    private Integer minIdle; //最小空闲连接数
    @Value("${redis-data.max-wait-millis}")
    private Long maxWaitMillis;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        // 连接池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(null == maxIdle ? 200 : maxIdle);
        poolConfig.setMinIdle(null == minIdle ? 50 : minIdle);
        poolConfig.setMaxTotal(null == maxTotal ? 10000 : maxTotal);
        poolConfig.setMaxWaitMillis(null == maxWaitMillis ? 5000L : maxWaitMillis);
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build();
        String[] address = nodes.split(",");
        if (isCluster) {
            //集群配置
            RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
            Set<RedisNode> nodeses = new HashSet<>();
            for (String addres : address) {
                String[] one = addres.split(":");
                String host = one[0];
                int port = Integer.valueOf(one[1]);
                nodeses.add(new RedisNode(host, port));
            }
            redisConfig.setClusterNodes(nodeses);
            // 跨集群执行命令时要遵循的最大重定向数量
            redisConfig.setMaxRedirects(3);
            redisConfig.setPassword(password);
            return new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
        } else {
            String addres = address[0];
            String[] single = addres.split(":");
            // 单节点模式
            RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
            redisConfig.setHostName(single[0]);
            redisConfig.setPort(Integer.valueOf(single[1]));
            if (password != null && !"".equals(password)) {
                redisConfig.setPassword(password);
            }
            return new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
        }
    }


    @Bean("redisHardRedisTemplate")
    public RedisTemplate redisHardRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return this.getRedisTemplate(lettuceConnectionFactory, true);
    }

    @Bean("lockRedisTemplate")
    public RedisTemplate lockRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate lockRedisTemplate = this.getRedisTemplate(lettuceConnectionFactory, false);
        RedisLockUtil.setLocker(lockRedisTemplate);
        return lockRedisTemplate;
    }

    /**
     * 模板序列化方案
     *
     * @param factory     连接工厂
     * @param entityModel 是否使用对象序列化方案
     * @return
     */
    private RedisTemplate getRedisTemplate(RedisConnectionFactory factory, boolean entityModel) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        if (entityModel) {
            //重写redis序列化方案 @author Ashy.Cheung 对象方案
            RedisSerializer<String> redisSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(redisSerializer);
            redisTemplate.setHashKeySerializer(redisSerializer);
            RedisObjectSerializer redisObjectSerializer = new RedisObjectSerializer();
            redisTemplate.setHashValueSerializer(redisObjectSerializer);
            redisTemplate.setValueSerializer(redisObjectSerializer);
        } else {
            //重写redis序列化方案 @author Ashy.Cheung 字符串方案
            StringRedisSerializer redisSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(redisSerializer);
            redisTemplate.setHashKeySerializer(redisSerializer);
            redisTemplate.setHashValueSerializer(redisSerializer);
            redisTemplate.setValueSerializer(redisSerializer);
        }
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}
