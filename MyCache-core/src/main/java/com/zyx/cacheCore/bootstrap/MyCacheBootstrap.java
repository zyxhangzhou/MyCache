package com.zyx.cacheCore.bootstrap;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEvict;
import com.zyx.cacheCore.assistance.evict.MyCacheEvictStrategy;
import com.zyx.cacheCore.assistance.util.ArgumentUtils;
import com.zyx.cacheCore.core.MyCache;
import com.zyx.cacheCore.core.MyCacheContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 16:58
 * @Description
 */
public class MyCacheBootstrap<K, V> {
    private MyCacheBootstrap() {
    }

    public static <K, V> MyCacheBootstrap<K, V> newInstance() {
        return new MyCacheBootstrap<>();
    }

    private Map<K, V> map = new HashMap<>();
    private int size = Integer.MAX_VALUE;
    private IMyCacheEvict<K, V> evict = MyCacheEvictStrategy.fifo();

    public MyCacheBootstrap<K, V> map(Map<K, V> map) {
        Objects.requireNonNull(map, "map is null!");
        this.map = map;
        return this;
    }

    public MyCacheBootstrap<K, V> size(int size) {
        ArgumentUtils.requirePositve(size, "size");
        this.size = size;
        return this;
    }

    public MyCacheBootstrap<K, V> evict(IMyCacheEvict<K, V> IMyCacheEvict) {
        this.evict = IMyCacheEvict;
        return this;
    }

    public IMyCache<K, V> build() {
        MyCacheContext<K, V> context = new MyCacheContext<>();
        context.cacheEvict(evict).map(map).size(size);
        //MyCache<K, V> cache = new MyCache<>(context);
        return new MyCache<>(context);
    }

}
