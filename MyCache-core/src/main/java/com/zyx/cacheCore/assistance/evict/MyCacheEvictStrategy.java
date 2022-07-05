package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.IMyCacheEvict;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 17:02
 * @Description
 */
public class MyCacheEvictStrategy {
    private MyCacheEvictStrategy() {}

    public static <K, V> IMyCacheEvict<K, V> fifo() {
        return new MyCacheEvictFIFO<>();
    }
}
