package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 16:40
 * @Description
 */
public interface IMyCacheEvict<K, V> {
    IMyCacheEntry<K,V> evict(IMyCacheEvictContext<K, V> context);
    void updateKey(final K key);
    void removeKey(final K key);
}
