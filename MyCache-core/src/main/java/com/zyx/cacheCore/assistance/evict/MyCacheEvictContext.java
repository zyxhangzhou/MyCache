package com.zyx.cacheCore.assistance.evict;

import cn.hutool.cache.Cache;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEvictContext;
import com.zyx.cacheCore.core.MyCache;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 16:13
 * @Description
 */

public class MyCacheEvictContext<K, V> implements IMyCacheEvictContext<K, V> {

    /**
     * 新加入的key
     */
    private K key;

    /**
     * cache的实现
     */
    private IMyCache<K, V> cache;

    /**
     * max size limit
     */
    private int size;

    @Override
    public K key() {
        return key;
    }

    public MyCacheEvictContext<K, V> key(K key) {
        this.key = key;
        return this;
    }

    @Override
    public IMyCache<K, V> cache() {
        return cache;
    }

    public MyCacheEvictContext<K, V> cache(MyCache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public int size() {
        return size;
    }

    public MyCacheEvictContext<K, V> size(int size) {
        this.size = size;
        return this;
    }

}
