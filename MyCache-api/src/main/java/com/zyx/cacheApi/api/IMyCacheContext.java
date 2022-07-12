package com.zyx.cacheApi.api;

import java.util.Map;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 19:40
 * @Description 缓存的上下文
 */
public interface IMyCacheContext<K, V> {
    /**
     * map 基本信息
     *
     * @return map
     */
    Map<K, V> map();

    /**
     * 大小限制，而不是大小
     *
     * @return 大小限制
     */
    int size();

    /**
     * 驱除策略，🈯️满了，会驱逐之前的信息
     *
     * @return 策略
     */
    IMyCacheEvict<K, V> cacheEvict();
}
