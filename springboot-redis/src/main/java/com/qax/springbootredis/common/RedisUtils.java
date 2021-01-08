package com.qax.springbootredis.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Guoqing.Qin
 * @ClassName RedisUtils
 * @create 2021-01-08 12:04
 * @Description:
 */
@Slf4j
@Component
@SuppressWarnings("ALL")
public class RedisUtils<T> {
    public RedisUtils() {
        super();
    }

    /**
     * StringRedisTemplate有默认的序列化器
     */
    @Autowired
    private RedisTemplate redisTemplate;

    public void convertAndSend(String channel, T message) {
        log.info("------------ 发送消息 channel:{},message={}", channel, message.toString());
        redisTemplate.convertAndSend(channel, message);
        log.info("------------发送消息结束---------------------- ");
    }


}
