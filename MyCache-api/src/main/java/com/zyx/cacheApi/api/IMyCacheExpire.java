package com.zyx.cacheApi.api;

import java.util.Collection;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/6 21:05
 * @Description 缓存过期处理接口
 */
public interface IMyCacheExpire<K, V> {

    /**
     * 指定过期信息
     *
     * @param key      key
     * @param expireAt 什么时候过期
     */
    void expire(final K key, final long expireAt);

    /**
     * 惰性删除中需要处理的 keys
     *
     * @param keyList keys
     */
    void refreshExpire(final Collection<K> keyList);

    /**
     * 待过期的 key
     * 不存在，则返回 null
     *
     * @param key 待过期的 key
     * @return 结果
     */
    Long expireTime(final K key);
}
