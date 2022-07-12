package com.zyx.cacheCore.core;

import com.zyx.cacheApi.api.IMyCacheContext;
import com.zyx.cacheApi.api.IMyCacheEvict;

import java.util.Map;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 19:42
 * @Description 缓存上下文
 */
public class MyCacheContext<K, V> implements IMyCacheContext<K, V> {

    /**
     * map 信息
     */
    private Map<K, V> map;

    /**
     * 大小限制
     */
    private int size;

    /**
     * 驱除策略
     */
    private IMyCacheEvict<K, V> cacheEvict;

    @Override
    public Map<K, V> map() {
        return map;
    }

    public MyCacheContext<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    @Override
    public int size() {
        return size;
    }

    public MyCacheContext<K, V> size(int size) {
        this.size = size;
        return this;
    }

    @Override
    public IMyCacheEvict<K, V> cacheEvict() {
        return cacheEvict;
    }

    public MyCacheContext<K, V> cacheEvict(IMyCacheEvict<K, V> cacheEvict) {
        this.cacheEvict = cacheEvict;
        return this;
    }
}
