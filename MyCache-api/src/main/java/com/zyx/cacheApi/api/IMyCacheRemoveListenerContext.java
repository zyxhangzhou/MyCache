package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 13:34
 * @Description 删除监听器的上下文
 */
public interface IMyCacheRemoveListenerContext<K, V> {

    /**
     * 待移除的key
     *
     * @return K
     */
    K key();

    /**
     * 待移除的值
     *
     * @return V
     */
    V value();

    /**
     * 删除类型
     *
     * @return String
     */
    String type();
}
