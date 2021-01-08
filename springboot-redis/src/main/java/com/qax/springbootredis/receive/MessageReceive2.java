package com.qax.springbootredis.receive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qax.springbootredis.bean.RedisMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @author Guoqing.Qin
 * @ClassName MessageReceive1
 * @create 2021-01-08 14:53
 * @Description: Redis接受消息客户端2
 */
@Slf4j
@Component
@SuppressWarnings("ALL")
public class MessageReceive2 {
    public MessageReceive2() {
        super();
    }


    public void getMessage(String object) {
        //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(RedisMessage.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        RedisMessage message = (RedisMessage) seria.deserialize(object.getBytes());
        log.info("消息客户端2号接收消息：{}", message.toString());
    }

}
