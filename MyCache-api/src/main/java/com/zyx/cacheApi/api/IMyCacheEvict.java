package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 16:40
 * @Description 驱逐策略的统一接口
 */
public interface IMyCacheEvict<K, V> {

    /**
     * 驱逐策略
     *
     * @param context 上下文
     * @return 没有时return null
     */
    IMyCacheEntry<K, V> evict(IMyCacheEvictContext<K, V> context);

    /**
     * 更新 key 信息
     *
     * @param key key
     */
    void updateKey(final K key);

    /**
     * 删除 key 信息
     *
     * @param key key
     */
    void removeKey(final K key);
}
