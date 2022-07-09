package com.zyx.cacheApi.api;

import java.util.List;
import java.util.Map;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/3 16:50
 * @Description
 */
public interface IMyCache<K, V> extends Map<K, V> {


    /**
     * 设置过期时间
     * @param key key
     * @param timeInMills 多少毫秒之后过期
     * @return this
     */
    IMyCache<K, V> expire(final K key, final long timeInMills);

    /**
     * 得到缓存的过期处理类
     * @return IMyCacheExpire
     */
    IMyCacheExpire<K,V> expire();

    IMyCache<K, V> expireAt(final K key, final long timeInMills);

    IMyCacheLoad<K,V> load();

    IMyCachePersist<K,V> persist();

    List<IMyCacheRemoveListener<K,V>> removeListeners();
}
