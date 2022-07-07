package com.zyx.cacheApi.api;

import java.util.Collection;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/6 21:05
 * @Description
 */
public interface IMyCacheExpire<K, V> {
    void expire(final K key, final long expireAt);
    void refreshExpire(final Collection<K> keyList);
    Long expireTime(final K key);
}
