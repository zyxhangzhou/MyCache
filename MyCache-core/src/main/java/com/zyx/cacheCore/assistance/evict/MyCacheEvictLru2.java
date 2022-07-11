package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.ILruMap;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEntry;
import com.zyx.cacheApi.api.IMyCacheEvictContext;
import com.zyx.cacheCore.model.MyCacheEntry;
import com.zyx.cacheCore.struct.lru.LruMapDoubleList;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 23:36
 * @Description 实现方式LRU2 <a href="http://www.cs.cmu.edu/~christos/courses/721-resources/p297-o_neil.pdf">LRU-2</a>
 */
@Slf4j
public class MyCacheEvictLru2<K, V> extends AbstractMyCacheEvict<K, V> {

    /**
     * 第一次访问的 lru
     * <p>
     * 相当于把2Q思想中的FIFO的队列换成了另一个LRU，<br>
     * 这样命中率更高，但更占空间
     */
    private final ILruMap<K, V> firstLruMap;

    /**
     * 2次及其以上的 lru
     */
    private final ILruMap<K, V> moreLruMap;

    public MyCacheEvictLru2() {
        this.firstLruMap = new LruMapDoubleList<>();
        this.moreLruMap = new LruMapDoubleList<>();
    }

    @Override
    protected IMyCacheEntry<K, V> doEvict(IMyCacheEvictContext<K, V> context) {
        IMyCacheEntry<K, V> result = null;
        final IMyCache<K, V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if (cache.size() >= context.size()) {
            IMyCacheEntry<K, V> evictEntry = null;

            //1. firstLruMap 不为空，优先移除队列中元素
            if (!firstLruMap.isEmpty()) {
                evictEntry = firstLruMap.removeEldest();
                log.debug("从 firstLruMap 中淘汰数据：{}", evictEntry);
            } else {
                //2. 否则从 moreLruMap 中淘汰数据
                evictEntry = moreLruMap.removeEldest();
                log.debug("从 moreLruMap 中淘汰数据：{}", evictEntry);
            }

            // 执行缓存移除操作
            final K evictKey = evictEntry.key();
            V evictValue = cache.remove(evictKey);
            result = new MyCacheEntry<>(evictKey, evictValue);
        }

        return result;
    }

    /**
     * 更新信息
     * 1. 如果 moreLruMap 已经存在，则处理 more 队列，先删除，再插入。
     * 2. 如果 firstLruMap 中已经存在，则处理 first 队列，先删除 firstLruMap，然后插入 Lru。
     * 1 和 2 是不同的场景，但是代码实际上是一样的，删除逻辑中做了二种场景的兼容。
     * <p>
     * 3. 如果不在1、2中，说明是新元素，直接插入到 firstLruMap 的开始即可。
     *
     * @param key 元素
     */
    @Override
    public void updateKey(final K key) {
        //1. 元素已经在多次访问，或者第一次访问的 lru 中
        if (moreLruMap.contains(key)
                || firstLruMap.contains(key)) {
            //1.1 删除信息
            this.removeKey(key);

            //1.2 加入到多次 LRU 中
            moreLruMap.updateKey(key);
            log.debug("key: {} 多次访问，加入到 moreLruMap 中", key);
        } else {
            // 2. 加入到第一次访问 LRU 中
            firstLruMap.updateKey(key);
            log.debug("key: {} 为第一次访问，加入到 firstLruMap 中", key);
        }
    }

    /**
     * 移除元素
     * <p>
     * 1. 多次 lru 中存在，删除
     * 2. 初次 lru 中存在，删除
     *
     * @param key 元素
     * @since 0.0.13
     */
    @Override
    public void removeKey(final K key) {
        //1. 多次LRU 删除逻辑
        if (moreLruMap.contains(key)) {
            moreLruMap.removeKey(key);
            log.debug("key: {} 从 moreLruMap 中移除", key);
        } else {
            firstLruMap.removeKey(key);
            log.debug("key: {} 从 firstLruMap 中移除", key);
        }
    }
}
