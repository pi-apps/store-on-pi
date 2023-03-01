package com.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("redisUtil")
public class RedisUtil {

    @Autowired
    @Qualifier("redisHardRedisTemplate")
    private RedisTemplate redisTemplate;

    public RedisTemplate redisTemplate() {
        return redisTemplate;
    }

    /**
     * 过期时间处理
     *
     * @param redisKey  key
     * @param redisVal  val
     * @param timeUnit  时间类型
     * @param timeOne   数字1
     * @param timeTwo   数字2
     * @param timeThree 数字3
     */
    public void setRedisListennerTimeOut(String redisKey, String redisVal, TimeUnit timeUnit, int timeOne, int timeTwo, int timeThree) {
        if (0 != timeOne) {
            this.redisTemplate().opsForValue().set(redisKey + redisVal + "-" + timeOne, redisVal, timeOne, timeUnit);

        }
        if (0 != timeTwo) {
            this.redisTemplate().opsForValue().set(redisKey + redisVal + "-" + timeTwo, redisVal, timeTwo, timeUnit);

        }
        if (0 != timeThree) {
            this.redisTemplate().opsForValue().set(redisKey + redisVal + "-" + timeThree, redisVal, timeThree, timeUnit);

        }
    }
}
