package com.zyx.cacheApi.api;

import java.util.Map;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/3 16:50
 * @Description
 */
public interface IMyCache<K, V> extends Map<K, V> {


    IMyCache<K, V> expire(final K key, final long timeInMills);

    IMyCache<K, V> expireAt(final K key, final long timeInMills);
}
