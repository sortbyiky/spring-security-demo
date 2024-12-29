package com.scholar.securitytest.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 缓存工具类
 * 提供常用的 Redis 操作方法，包括对字符串、列表、集合、哈希等数据类型的缓存操作。
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisCache {

    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本对象（如 Integer、String、实体类等）
     *
     * @param key   缓存的键
     * @param value 缓存的值
     * @param <T>   对象类型
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本对象并设置有效期
     *
     * @param key      缓存的键
     * @param value    缓存的值
     * @param timeout  有效期
     * @param timeUnit 时间单位
     * @param <T>      对象类型
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置缓存键的有效期
     *
     * @param key     缓存的键
     * @param timeout 有效期（秒）
     * @return 是否设置成功
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存键的有效期（带时间单位）
     *
     * @param key     缓存的键
     * @param timeout 有效期
     * @param unit    时间单位
     * @return 是否设置成功
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取缓存的基本对象
     *
     * @param key 缓存的键
     * @param <T> 返回值类型
     * @return 缓存对象
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个缓存对象
     *
     * @param key 缓存的键
     * @return 是否删除成功
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个缓存对象
     *
     * @param collection 缓存键集合
     * @return 删除的数量
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存 List 数据
     *
     * @param key      缓存的键
     * @param dataList 缓存的列表数据
     * @param <T>      列表元素类型
     * @return 缓存成功的数量
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获取缓存的 List 数据
     *
     * @param key 缓存的键
     * @param <T> 列表元素类型
     * @return 缓存的 List 数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存 Set 数据
     *
     * @param key     缓存的键
     * @param dataSet 缓存的 Set 数据
     * @param <T>     Set 元素类型
     * @return 缓存操作对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        for (T value : dataSet) {
            setOperation.add(value);
        }
        return setOperation;
    }

    /**
     * 获取缓存的 Set 数据
     *
     * @param key 缓存的键
     * @param <T> Set 元素类型
     * @return 缓存的 Set 数据
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存 Map 数据
     *
     * @param key     缓存的键
     * @param dataMap 缓存的 Map 数据
     * @param <T>     Map 值的类型
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获取缓存的 Map 数据
     *
     * @param key 缓存的键
     * @param <T> Map 值的类型
     * @return 缓存的 Map 数据
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 向 Hash 中添加数据
     *
     * @param key   缓存的键
     * @param hKey  Hash 的键
     * @param value Hash 的值
     * @param <T>   值的类型
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取 Hash 中的单个值
     *
     * @param key  缓存的键
     * @param hKey Hash 的键
     * @param <T>  值的类型
     * @return Hash 的值
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        return (T) redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 删除 Hash 中的某个值
     *
     * @param key  缓存的键
     * @param hKey Hash 的键
     */
    public void delCacheMapValue(final String key, final String hKey) {
        redisTemplate.opsForHash().delete(key, hKey);
    }

    /**
     * 获取 Hash 中的多个值
     *
     * @param key   缓存的键
     * @param hKeys Hash 的键集合
     * @param <T>   值的类型
     * @return Hash 中的多个值
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 根据前缀获取缓存的键列表
     *
     * @param pattern 键的前缀
     * @return 匹配的键列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }
}