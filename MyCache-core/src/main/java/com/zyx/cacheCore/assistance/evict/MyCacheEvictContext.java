package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEvictContext;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 16:13
 * @Description
 */
@Setter
@Accessors(chain = true)
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

    @Override
    public IMyCache<K, V> cache() {
        return cache;
    }

    @Override
    public int size() {
        return size;
    }

}
