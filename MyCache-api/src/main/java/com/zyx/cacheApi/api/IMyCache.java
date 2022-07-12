package com.zyx.cacheApi.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/3 16:50
 * @Description 缓存的统一接口
 */
public interface IMyCache<K, V> extends Map<K, V> {


    /**
     * 设置过期时间
     * 若key不存在，直接返回
     * <p>
     * 采用redis中的惰性删除思想
     * <p>
     * 当指定key被惰性删除时，可能还没有真正删除。
     * 引用的refresh方法解决了上述问题，如
     * {@link IMyCacheExpire#refreshExpire(Collection)}
     * <p>
     * 还实现了定时删除功能
     * <p>
     * 简单的实现方式：启动一个定时任务，
     * 每次选择指定大小的key进行是否过期判断
     *
     * @param key         key
     * @param timeInMills 多少毫秒之后过期
     * @return this
     */
    IMyCache<K, V> expire(final K key, final long timeInMills);

    /**
     * 得到缓存的过期处理类
     *
     * @return IMyCacheExpire
     */
    IMyCacheExpire<K, V> expire();

    /**
     * 在指定的时间<b>之后</b>过期
     *
     * @param key         key
     * @param timeInMills 时间
     * @return this
     */
    IMyCache<K, V> expireAt(final K key, final long timeInMills);

    /**
     * 从别处加载缓存信息
     *
     * @return 加载的信息
     */
    IMyCacheLoad<K, V> load();

    /**
     * 持久化操作
     *
     * @return 持久化统一接口
     */
    IMyCachePersist<K, V> persist();

    /**
     * 淘汰策略
     *
     * @return 淘汰策略
     */
    IMyCacheEvict<K, V> evict();

    /**
     * 删除监听类列表
     *
     * @return 监听器列表
     */
    List<IMyCacheRemoveListener<K, V>> removeListeners();

    /**
     * 慢日志监听类列表
     *
     * @return 监听器列表
     */
    List<IMyCacheSlowListener> slowListeners();
}
