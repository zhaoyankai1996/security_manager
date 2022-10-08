package com.inspur.labor.security.util;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * UAC Hessain 序列化
 *
 * @param <T>
 * @author zyk
 */
public class UacHessainRedisSerializer<T> implements RedisSerializer<T> {

    @Override
    public byte[] serialize(T t) throws SerializationException {
        return HessianUtils.serialize(t);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (null == bytes) {
            return null;
        }
        return HessianUtils.unserizal(bytes);
    }
}
