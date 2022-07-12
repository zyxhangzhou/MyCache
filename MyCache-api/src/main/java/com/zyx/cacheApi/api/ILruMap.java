package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 23:26
 * @Description 封装好的LRU-map
 */
public interface ILruMap<K, V> {
    /**
     * 移除最老的元素
     * @return 移除的明细
     */
    IMyCacheEntry<K, V> removeEldest();

    /**
     * 更新 key 的信息
     * @param key key
     */
    void updateKey(final K key);

    /**
     * 移除对应的 key 信息
     * @param key key
     */
    void removeKey(final K key);

    /**
     * 是否为空
     * @return 是否
     */
    boolean isEmpty();

    /**
     * 是否包含元素
     * @param key 元素
     * @return 结果
     */
    boolean contains(final K key);
}
