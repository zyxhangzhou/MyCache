package com.zyx.cacheApi.api;

import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 17:18
 * @Description 持久化的接口
 */
public interface IMyCachePersist<K, V> {

    /**
     * 持久化缓存信息
     *
     * @param cache 缓存
     */
    void persist(final IMyCache<K, V> cache);

    /**
     * 延迟的时间，🈯️什么时候做第一次
     *
     * @return 延迟
     */
    long delay();

    /**
     * 时间间隔
     *
     * @return 间隔
     */
    long period();

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit timeUnit();
}
