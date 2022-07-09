package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 13:34
 * @Description
 */
public interface IMyCacheRemoveListenerContext<K, V> {
    K key();
    V value();
    String type();
}
