package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.IMyCacheEntry;
import com.zyx.cacheApi.api.IMyCacheEvictContext;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 14:53
 * @Description
 */
public class MyCacheEvictNone<K,V> extends AbstractMyCacheEvict<K,V> {
    @Override
    protected IMyCacheEntry<K, V> doEvict(IMyCacheEvictContext<K, V> context) {
        return null;
    }
}
