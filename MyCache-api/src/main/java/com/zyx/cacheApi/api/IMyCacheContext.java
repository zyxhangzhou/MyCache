package com.zyx.cacheApi.api;

import java.util.Map;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 19:40
 * @Description
 */
public interface IMyCacheContext<K, V> {
    Map<K, V> map();

    int size();

    IMyCacheEvict<K, V> cacheEvict();
}
