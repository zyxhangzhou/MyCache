package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEntry;
import com.zyx.cacheApi.api.IMyCacheEvictContext;
import com.zyx.cacheCore.model.MyCacheEntry;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 14:22
 * @Description LRU策略 最近最少使用 朴素实现
 * 这个实现有需要优化的地方，因为remove操作是一个O(n)的操作 <p>
 * 访问一个数据时，当数据不在链表中，则将数据插入至链表头部，
 * 如果在链表中，则将该数据移至链表头部。当数据空间已满后，
 * 则淘汰链表最末尾的数据。
 */
@Slf4j
public class MyCacheEvictLru<K, V> extends AbstractMyCacheEvict<K, V> {

    private final List<K> list = new LinkedList<>();

    @Override
    protected IMyCacheEntry<K, V> doEvict(IMyCacheEvictContext<K, V> context) {
        IMyCacheEntry<K, V> result = null;
        final IMyCache<K, V> cache = context.cache();
        // 超过限制，移除队尾的元素
        if (cache.size() >= context.size()) {
            K evictKey = list.get(list.size() - 1);
            V evictValue = cache.remove(evictKey);
            result = new MyCacheEntry<>(evictKey, evictValue);
        }
        return result;
    }

    /**
     * 放入元素
     * ① 删除已经存在的
     * ② 新元素放到元素头部
     *
     * @param key 元素
     */
    @Override
    public void updateKey(K key) {
        list.remove(key);
        list.add(0, key);
    }

    @Override
    public void removeKey(K key) {
        list.remove(key);
    }
}
