package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 15:51
 * @Description 驱逐策略上下文
 */
public interface IMyCacheEvictContext<K, V> {

    /**
     * 新加入的key
     *
     * @return key
     */
    K key();

    /**
     * cache的实现
     *
     * @return 对应的map
     */
    IMyCache<K, V> cache();

    /**
     * 返回大小
     *
     * @return int
     */
    int size();
}
