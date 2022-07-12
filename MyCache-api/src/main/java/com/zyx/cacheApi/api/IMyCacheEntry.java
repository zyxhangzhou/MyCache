package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 16:38
 * @Description 缓存的键值对信息
 */
public interface IMyCacheEntry<K, V> {

    /**
     * 键
     *
     * @return K
     */
    K key();

    /**
     * 值
     *
     * @return V
     */
    V value();
}
