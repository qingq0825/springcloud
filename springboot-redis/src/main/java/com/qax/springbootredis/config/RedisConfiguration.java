package com.qax.springbootredis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qax.springbootredis.receive.MessageReceive1;
import com.qax.springbootredis.receive.MessageReceive2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;


/**
 * @author Guoqing.Qin
 * @ClassName RedisConfiguration
 * @create 2021-01-08 14:26
 * @Description:配置Redis序列化器
 */
@ConfigurationProperties(prefix = "redis.match")
@Configuration
@SuppressWarnings("ALL")
public class RedisConfiguration {
    public RedisConfiguration() {
        super();
    }

    private List<String> queues;

    public void setQueues(List<String> queues) {
        this.queues = queues;
    }


    /**
     * 对于字符串的可以使用自带的StringRedisTemplate来进行实例化
     * 其他类型可以自定义序列化器
     *
     * @param redisConnectionFactory
     *
     * @return
     */
    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, T> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认的jdkSerializeable序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;

    }

    /**
     * redis消息监听器容器
     * 以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     *
     * @param connectionFactory
     * @param listenerAdapter
     *
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter1,
                                                   MessageListenerAdapter listenerAdapter2) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // 遍历匹配规则，监听频道
        for (String queue : queues) {

            // 订阅多个频道,listenerAdapter1可以接收发送到test1、test2的消息
//            container.addMessageListener(listenerAdapter1, new ChannelTopic(queue));
            container.addMessageListener(listenerAdapter1, new PatternTopic(queue));

            //不同的订阅者，listenerAdapter2可以接收test2的消息
            container.addMessageListener(listenerAdapter2, new PatternTopic(queue));
        }

        // 序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        container.setTopicSerializer(seria);
        return container;
    }

    /**
     * 表示客户端监听一个频道
     *
     * @param MessageReceive1 客户端1,利用反射执行getMessage方法
     */
    @Bean
    public MessageListenerAdapter listenerAdapter1(MessageReceive1 messageReceive1) {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“MessageReceive1 ”
        return new MessageListenerAdapter(messageReceive1, "getMessage");
    }


    /**
     * 表示监听一个频道
     *
     * @param MessageReceive1 客户端2,利用反射执行getMessage方法
     */
    @Bean
    public MessageListenerAdapter listenerAdapter2(MessageReceive2 messageReceive2) {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“MessageReceive2 ”
        return new MessageListenerAdapter(messageReceive2, "getMessage");
    }

}
