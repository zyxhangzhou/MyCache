package com.zyx.cacheCore.model;

import com.zyx.cacheApi.api.IMyCacheEntry;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 20:21
 * @Description 缓存的节点信息
 */
public class MyCacheEntry<K, V> implements IMyCacheEntry<K, V> {

    /**
     * 键
     */
    private final K key;

    /**
     * 值
     */
    private final V value;

    public MyCacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> MyCacheEntry<K, V> of(final K key,
                                               final V value) {
        return new MyCacheEntry<>(key, value);
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public String toString() {
        return "MyCacheEntry{" + "key=" + key +
                ", value=" + value +
                '}';
    }
}
