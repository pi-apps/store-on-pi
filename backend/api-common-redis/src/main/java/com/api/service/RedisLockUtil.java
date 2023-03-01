package com.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.StandardCharsets;

public class RedisLockUtil {

    private static final Logger log = LoggerFactory.getLogger(RedisLockUtil.class);

    private static RedisTemplate<String, Object> redisTemplate;

    public static void setLocker(RedisTemplate lockRedisTemplate) {
        redisTemplate = lockRedisTemplate;
    }

    /**
     * 释放锁脚本，原子操作，lua脚本
     */
    private static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 获取分布式锁，原子操作
     *
     * @param lockKey   锁
     * @param lockValue 唯一ID
     * @param leaseTime 过期时间 秒
     * @return 是否枷锁成功
     */
    public static boolean tryLock(String lockKey, String lockValue, long leaseTime) {
        try {
            RedisCallback<Boolean> callback = (connection) -> connection.set(lockKey.getBytes(StandardCharsets.UTF_8),
                    lockValue.getBytes(StandardCharsets.UTF_8), Expiration.seconds(leaseTime),
                    RedisStringCommands.SetOption.SET_IF_ABSENT);
            return redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error ,lock key: {}, value : {}, error info : {}", lockKey, lockValue, e);
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey   锁
     * @param lockValue 唯一ID
     * @return 执行结果
     */
    public static boolean unlock(String lockKey, String lockValue) {
        RedisCallback<Boolean> callback = (connection) -> connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1, lockKey.getBytes(StandardCharsets.UTF_8), lockValue.getBytes(StandardCharsets.UTF_8));
        return redisTemplate.execute(callback);
    }

    /**
     * 获取分布式锁，该方法不再使用
     *
     * @param lockKey   锁
     * @param lockValue 唯一ID
     * @param waitTime  等待时间 秒
     * @param leaseTime 过期时间 秒
     * @return 是否枷锁成功
     */
    @Deprecated
    public static boolean tryLock(String lockKey, String lockValue, long waitTime, long leaseTime) {
        try {
            RedisCallback<Boolean> callback = (connection) -> connection.set(lockKey.getBytes(StandardCharsets.UTF_8),
                    lockValue.getBytes(StandardCharsets.UTF_8), Expiration.seconds(leaseTime),
                    RedisStringCommands.SetOption.SET_IF_ABSENT);
            return redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error ,lock key: {}, value : {}, error info : {}", lockKey, lockValue, e);
        }
        return false;
    }
}