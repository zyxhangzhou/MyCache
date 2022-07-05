package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.IMyCacheEntry;
import com.zyx.cacheApi.api.IMyCacheEvict;
import com.zyx.cacheApi.api.IMyCacheEvictContext;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 19:51
 * @Description
 */
public abstract class AbstractMyCacheEvict<K, V> implements IMyCacheEvict<K, V> {
    @Override
    public IMyCacheEntry<K,V> evict(IMyCacheEvictContext<K, V> context) {
        return doEvict(context);
    }

    protected abstract IMyCacheEntry<K,V> doEvict(IMyCacheEvictContext<K, V> context);

    @Override
    public void updateKey(K key) {

    }

    @Override
    public void removeKey(K key) {

    }
}
