package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/7 23:23
 * @Description 加载接口
 */
public interface IMyCacheLoad<K, V> {

    /**
     * 加载缓存信息
     *
     * @param cache 缓存
     */
    void load(final IMyCache<K, V> cache);
}
