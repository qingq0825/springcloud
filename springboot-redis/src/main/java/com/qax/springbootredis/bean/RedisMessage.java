package com.qax.springbootredis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Guoqing.Qin
 * @ClassName RedisMessage
 * @create 2021-01-08 14:54
 * @Description:
 */
@Data
@ToString
@AllArgsConstructor
@SuppressWarnings("ALL")
public class RedisMessage<T> implements Serializable {
    private static final long serialVersionUID = -8807348856934698637L;

    public RedisMessage() {
        super();
    }

    private Integer id;

    private String name;

    private Integer age;

    private String address;

}
