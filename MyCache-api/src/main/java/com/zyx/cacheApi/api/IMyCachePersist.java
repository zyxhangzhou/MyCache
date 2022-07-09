package com.zyx.cacheApi.api;

import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 17:18
 * @Description
 */
public interface IMyCachePersist<K, V> {
    void persist(final IMyCache<K, V> cache);

    /**
     * 延迟的时间
     * @return 延迟
     */
    long delay();

    /**
     * 时间间隔
     * @return 间隔
     */
    long period();

    TimeUnit timeUnit();
}
