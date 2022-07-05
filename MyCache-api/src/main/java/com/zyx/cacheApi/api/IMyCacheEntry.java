package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 16:38
 * @Description
 */
public interface IMyCacheEntry<K, V> {
    K key();

    V value();
}
