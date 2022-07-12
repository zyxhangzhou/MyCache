package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEntry;
import com.zyx.cacheApi.api.IMyCacheEvict;
import com.zyx.cacheApi.api.IMyCacheEvictContext;
import com.zyx.cacheCore.model.MyCacheEntry;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 16:42
 * @Description LRU 通过继承 LinkHashMap 实现
 */
@Slf4j
public class MyCacheEvictLruLinkedHashMap<K, V> extends LinkedHashMap<K, V> implements IMyCacheEvict<K, V> {

    /**
     * 是否移除标识
     */
    private volatile boolean removeFlag = false;

    /**
     * 最旧的一个元素
     */
    private transient Map.Entry<K, V> eldest = null;

    public MyCacheEvictLruLinkedHashMap() {
        super(16, 0.75f, true);
    }

    @Override
    public IMyCacheEntry<K, V> evict(IMyCacheEvictContext<K, V> context) {
        IMyCacheEntry<K, V> result = null;
        final IMyCache<K,V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if(cache.size() >= context.size()) {
            removeFlag = true;

            // 执行 put 操作
            super.put(context.key(), null);

            // 构建淘汰的元素
            K evictKey = eldest.getKey();
            V evictValue = cache.remove(evictKey);
            result = new MyCacheEntry<>(evictKey, evictValue);
        } else {
            removeFlag = false;
        }

        return result;
    }

    @Override
    public void updateKey(K key) {
        super.put(key, null);
    }

    @Override
    public void removeKey(K key) {
        super.remove(key);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        this.eldest = eldest;
        return removeFlag;
    }
}
