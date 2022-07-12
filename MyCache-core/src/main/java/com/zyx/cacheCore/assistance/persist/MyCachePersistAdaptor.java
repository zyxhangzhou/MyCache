package com.zyx.cacheCore.assistance.persist;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCachePersist;

import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 20:14
 * @Description 缓存持久化-适配器模式
 */
public class MyCachePersistAdaptor<K, V> implements IMyCachePersist<K, V> {

    @Override
    public void persist(IMyCache<K, V> cache) {

    }

    @Override
    public long delay() {
        return this.period();
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
