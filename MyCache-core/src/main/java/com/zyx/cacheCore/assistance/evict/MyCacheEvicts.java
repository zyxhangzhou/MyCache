package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.IMyCacheEvict;

import java.util.LinkedHashMap;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 14:52
 * @Description
 */
public class MyCacheEvicts {
    private MyCacheEvicts() {
    }

    /**
     * 无策略
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     */
    public static <K, V> IMyCacheEvict<K, V> none() {
        return new MyCacheEvictNone<>();
    }

    /**
     * 先进先出
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     */
    public static <K, V> IMyCacheEvict<K, V> fifo() {
        return new MyCacheEvictFifo<>();
    }

    /**
     * LRU 驱除策略
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     */
    public static <K, V> IMyCacheEvict<K, V> lru() {
        return new MyCacheEvictLru<>();
    }

    /**
     * LRU 驱除策略
     * <p>
     * 基于双向链表 + map 实现，即模拟 {@link LinkedHashMap }
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     */
    public static <K, V> IMyCacheEvict<K, V> lruDoubleListMap() {
        return new MyCacheEvictLruDoubleListMap<>();
    }


    /**
     * LRU 驱除策略
     * <p>
     * 基于LinkedHashMap
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     */
    public static <K, V> IMyCacheEvict<K, V> lruLinkedHashMap() {
        return new MyCacheEvictLruLinkedHashMap<>();
    }

    /**
     * LRU 驱除策略
     * <p>
     * 基于 2Q 实现
     *
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @see <a href="http://www.vldb.org/conf/1994/P439.PDF">http://www.vldb.org/conf/1994/P439.PDF</a>
     */
    public static <K, V> IMyCacheEvict<K, V> lru2Q() {
        return new MyCacheEvictLru2Q<>();
    }

    /**
     * LRU 驱除策略
     *
     * 基于 LRU-2 实现
     * @param <K> key
     * @param <V> value
     * @return 结果
     * @see <a href="http://www.cs.cmu.edu/~christos/courses/721-resources/p297-o_neil.pdf">LRU-2</a>
     */
    public static <K, V> IMyCacheEvict<K, V> lru2() {
        return new MyCacheEvictLru2<>();
    }


}
