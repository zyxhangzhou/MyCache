package com.zyx.cacheCore.struct.lru;

import com.zyx.cacheApi.api.ILruMap;
import com.zyx.cacheApi.api.IMyCacheEntry;
import com.zyx.cacheCore.model.DoubleListNode;
import com.zyx.cacheCore.model.MyCacheEntry;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 23:29
 * @Description 将LRU + map简单的封装为一个数据结构
 */
@Slf4j
public class LruMapDoubleList<K, V> implements ILruMap<K, V> {

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
    private final Map<K, DoubleListNode<K, V>> indexMap;

    public LruMapDoubleList() {
        this.indexMap = new HashMap<>();
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();

        this.head.next(this.tail);
        this.tail.pre(this.head);
    }

    @Override
    public IMyCacheEntry<K, V> removeEldest() {
        // 获取尾巴节点的前一个元素
        DoubleListNode<K, V> tailPre = this.tail.pre();
        if (tailPre == this.head) {
            log.error("当前列表为空，无法进行删除");
            throw new RuntimeException("不可删除头结点!");
        }

        K evictKey = tailPre.key();
        V evictValue = tailPre.value();

        // 执行删除
        this.removeKey(evictKey);

        return MyCacheEntry.of(evictKey, evictValue);
    }

    /**
     * 放入元素
     * <p>
     * （1）删除已经存在的
     * （2）新元素放到元素头部
     *
     * @param key 元素
     */
    @Override
    public void updateKey(K key) {
        //1. 执行删除
        this.removeKey(key);

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
        indexMap.put(key, newNode);
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
    public void removeKey(K key) {
        DoubleListNode<K, V> node = indexMap.get(key);

        if (null == node) return;

        // 删除 list node
        // A<->B<->C
        // 删除 B，需要变成： A<->C
        DoubleListNode<K, V> pre = node.pre();
        DoubleListNode<K, V> next = node.next();

        pre.next(next);
        next.pre(pre);

        // 删除 map 中对应信息
        this.indexMap.remove(key);
        log.debug("从 LruMapDoubleList 中移除 key: {}", key);
    }

    @Override
    public boolean isEmpty() {
        return indexMap.isEmpty();
    }

    @Override
    public boolean contains(K key) {
        return indexMap.containsKey(key);
    }
}
