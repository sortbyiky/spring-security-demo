package com.scholar.securitytest.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

// 自定义 FastJson 序列化工具类，用于 Redis 中的对象序列化
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
    // 默认字符集 UTF-8
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    
    // 用于指定目标类类型
    private Class<T> clazz;

    // 静态初始化，开启 FastJson 自动类型支持
    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    // 构造函数，传入目标类类型
    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    // 序列化方法，将对象转换为字节数组
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];  // 如果对象为空，返回空字节数组
        }
        // 使用 FastJson 将对象转换为 JSON 字符串并返回字节数组
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    // 反序列化方法，将字节数组转换为对象
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;  // 如果字节数组为空，返回空对象
        }
        // 将字节数组转换为字符串，然后使用 FastJson 将字符串解析成对象
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz);
    }

    // 用于获取目标类的 Java 类型信息
    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}
