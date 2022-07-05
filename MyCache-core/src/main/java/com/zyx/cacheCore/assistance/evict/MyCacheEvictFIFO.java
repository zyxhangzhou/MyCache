package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEvictContext;
import com.zyx.cacheCore.model.MyCacheEntry;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 16:45
 * @Description
 */
public class MyCacheEvictFIFO<K, V> extends AbstractMyCacheEvict<K,V> {

    private final Queue<K> queue = new LinkedList<>();
    @Override
    protected MyCacheEntry<K, V> doEvict(IMyCacheEvictContext<K, V> context) {
        MyCacheEntry<K,V> result = null;
        final IMyCache<K,V> cache = context.cache();
        // 超过限制，执行移除
        if(cache.size() >= context.size()) {
            K evictKey = queue.remove();
            // 移除最开始的元素
            V evictValue = cache.remove(evictKey);
            result = new MyCacheEntry<>(evictKey, evictValue);
        }

        // 将新加的元素放入队尾
        final K key = context.key();
        queue.add(key);
        return result;
    }
}
