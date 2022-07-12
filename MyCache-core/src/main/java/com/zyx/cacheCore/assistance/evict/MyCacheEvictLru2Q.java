package com.zyx.cacheCore.assistance.evict;

import cn.hutool.core.util.ObjectUtil;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEntry;
import com.zyx.cacheApi.api.IMyCacheEvictContext;
import com.zyx.cacheCore.model.DoubleListNode;
import com.zyx.cacheCore.model.MyCacheEntry;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 21:19
 * @Description 实现2Q版的LRU
 */
@Slf4j
public class MyCacheEvictLru2Q<K, V> extends AbstractMyCacheEvict<K, V> {

    /**
     * 队列大小限制
     * <p>
     * 降低 O(n) 的消耗，避免耗时过长。
     */
    private static final int LIMIT_QUEUE_SIZE = 1024;

    /**
     * 第一次访问的队列
     */
    private final Queue<K> firstQueue;

    /**
     * 头结点
     */
    private final DoubleListNode<K, V> head;

    /**
     * 尾巴结点
     */
    private final DoubleListNode<K, V> tail;

    /**
     * map 信息
     * <p>
     * key: 元素信息
     * value: 元素在 list 中对应的节点信息
     */
    private final Map<K, DoubleListNode<K, V>> lruIndexMap;

    public MyCacheEvictLru2Q() {
        this.firstQueue = new ArrayDeque<>();
        this.lruIndexMap = new HashMap<>();
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();

        //首尾相连
        this.head.next(this.tail);
        this.tail.pre(this.head);
    }

    /**
     * 放入元素
     * 1. 如果 lruIndexMap 已经存在，则处理 lru 队列，先删除，再插入。
     * 2. 如果 firstQueue 中已经存在，则处理 first 队列，先删除 firstQueue，然后插入 Lru。
     * 1 和 2 是不同的场景，但是代码实际上是一样的，删除逻辑中做了二种场景的兼容。
     * <p>
     * 3. 如果不在1、2中，说明是新元素，直接插入到 firstQueue 的开始即可。
     *
     * @param key 元素
     * @since 0.0.13
     */
    @Override
    public void updateKey(final K key) {
        //1.1 是否在 LRU MAP 中
        //1.2 是否在 firstQueue 中
        DoubleListNode<K, V> node = lruIndexMap.get(key);
        if (null != node || firstQueue.contains(key)) {
            //1.3 删除信息
            this.removeKey(key);

            //1.4 加入到 LRU 中
            this.addToLruMapHead(key);
            return;
        }
        firstQueue.add(key);
    }

    /**
     * 移除元素
     * <p>
     * 1. 获取 map 中的元素
     * 2. 不存在直接返回，存在执行以下步骤：
     * 2.1 删除双向链表中的元素
     * 2.2 删除 map 中的元素
     *
     * @param key 元素
     */
    @Override
    public void removeKey(final K key) {
        DoubleListNode<K, V> node = lruIndexMap.get(key);

        //1. LRU 删除逻辑
        if (null != node) {
            // A<->B<->C
            // 删除 B，需要变成： A<->C
            DoubleListNode<K, V> pre = node.pre();
            DoubleListNode<K, V> next = node.next();

            pre.next(next);
            next.pre(pre);

            // 删除 map 中对应信息
            this.lruIndexMap.remove(node.key());
        } else {
            //2. FIFO 删除逻辑（O(n) 时间复杂度）
            firstQueue.remove(key);
        }
    }

    /**
     * 插入到 LRU Map 头部
     *
     * @param key 元素
     * @since 0.0.13
     */
    private void addToLruMapHead(final K key) {
        //2. 新元素插入到头部
        //head<->next
        //变成：head<->new<->next
        DoubleListNode<K, V> newNode = new DoubleListNode<>();
        newNode.key(key);

        DoubleListNode<K, V> next = this.head.next();
        this.head.next(newNode);
        newNode.pre(this.head);
        next.pre(newNode);
        newNode.next(next);

        //2.2 插入到 map 中
        lruIndexMap.put(key, newNode);
    }

    /**
     * 当缓存大小，已经达到最大限制时执行：
     * <p>
     * （1）优先淘汰 firstQueue 中的数据
     * <p>
     * （2）如果 firstQueue 中数据为空，则淘汰 lruMap 中的数据信息。
     * <p>
     * 假设：被多次访问的数据，重要性高于只被访问了一次的数据。
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
            K evictKey;

            //1. firstQueue 不为空，优先移除队列中元素
            if (!firstQueue.isEmpty()) {
                evictKey = firstQueue.remove();
            } else {
                // 获取尾巴节点的前一个元素
                DoubleListNode<K, V> tailPre = this.tail.pre();
                if (tailPre == this.head) {
                    log.error("当前列表为空，无法进行删除");
                    throw new RuntimeException("不可删除头结点!");
                }

                evictKey = tailPre.key();
            }

            // 执行移除操作
            V evictValue = cache.remove(evictKey);
            result = new MyCacheEntry<>(evictKey, evictValue);
        }

        return result;
    }
}
