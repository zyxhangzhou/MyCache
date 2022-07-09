package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/7 23:23
 * @Description
 */
public interface IMyCacheLoad<K, V> {
    void load(final IMyCache<K,V> cache);
}
