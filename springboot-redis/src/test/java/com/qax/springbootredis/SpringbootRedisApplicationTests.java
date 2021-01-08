package com.qax.springbootredis;

import com.qax.springbootredis.bean.RedisMessage;
import com.qax.springbootredis.common.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SpringbootRedisApplicationTests {

    @Autowired
    private RedisUtils<RedisMessage> redisUtils;

    @Test
    void contextLoads() {

        for (int i = 0; i < 100; i++) {
            RedisMessage message = new RedisMessage(i, "二号", 13, "北京市");
            redisUtils.convertAndSend("test2", message);
        }
        log.info("contextLoads 执行结束");
    }

}
