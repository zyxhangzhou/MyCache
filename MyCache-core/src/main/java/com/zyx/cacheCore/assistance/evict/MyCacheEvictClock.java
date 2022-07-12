package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.ILruMap;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEntry;
import com.zyx.cacheApi.api.IMyCacheEvictContext;
import com.zyx.cacheCore.model.MyCacheEntry;
import com.zyx.cacheCore.struct.lru.LruMapCircleList;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/12 14:12
 * @Description 驱逐策略-clock算法
 */
@Slf4j
public class MyCacheEvictClock<K, V> extends AbstractMyCacheEvict<K, V> {

    /**
     * 循环链表
     */
    private final ILruMap<K, V> circleList;

    public MyCacheEvictClock() {
        this.circleList = new LruMapCircleList<>();
    }

    /**
     * 执行驱逐
     *
     * @param context 上下文
     * @return IMyCacheEntry
     */
    @Override
    protected IMyCacheEntry<K, V> doEvict(IMyCacheEvictContext<K, V> context) {
        IMyCacheEntry<K, V> result = null;
        final IMyCache<K, V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if (cache.size() >= context.size()) {
            IMyCacheEntry<K, V> evictEntry = circleList.removeEldest();
            // 执行缓存移除操作
            final K evictKey = evictEntry.key();
            V evictValue = cache.remove(evictKey);

            log.debug("基于 clock 算法淘汰 key：{}, value: {}", evictKey, evictValue);
            result = new MyCacheEntry<>(evictKey, evictValue);
        }

        return result;
    }

    /**
     * 更新信息
     *
     * @param key 元素
     */
    @Override
    public void updateKey(final K key) {
        this.circleList.updateKey(key);
    }

    /**
     * 移除元素
     *
     * @param key 元素
     */
    @Override
    public void removeKey(final K key) {
        this.circleList.removeKey(key);
    }

}
