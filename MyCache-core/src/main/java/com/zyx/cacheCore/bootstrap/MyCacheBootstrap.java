package com.zyx.cacheCore.bootstrap;

import cn.hutool.cache.Cache;
import com.zyx.cacheApi.api.*;
import com.zyx.cacheCore.assistance.evict.MyCacheEvictStrategy;
import com.zyx.cacheCore.assistance.listener.remove.MyCacheRemoveListeners;
import com.zyx.cacheCore.assistance.load.MyCacheLoads;
import com.zyx.cacheCore.assistance.persist.MyCachePersists;
import com.zyx.cacheCore.assistance.util.ArgumentUtils;
import com.zyx.cacheCore.core.MyCache;
import com.zyx.cacheCore.core.MyCacheContext;

import java.util.HashMap;
import java.util.List;
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

    private final List<IMyCacheRemoveListener<K,V>> removeListeners = MyCacheRemoveListeners.defaults();

    private IMyCacheLoad<K, V> load = MyCacheLoads.none();

    private IMyCachePersist<K,V> persist = MyCachePersists.none();

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

    public MyCacheBootstrap<K, V> load(IMyCacheLoad<K, V> load) {
        ArgumentUtils.requireNotNull(load, "load");
        this.load = load;
        return this;
    }

    public MyCacheBootstrap<K, V> addRemoveListener(IMyCacheRemoveListener<K, V> removeListener) {
        ArgumentUtils.requireNotNull(removeListener, "remove listener");
        this.removeListeners.add(removeListener);
        return this;
    }

    public MyCacheBootstrap<K, V> persist(IMyCachePersist<K, V> persist) {
        this.persist = persist;
        return this;
    }


    public IMyCache<K, V> build() {
        MyCache<K, V> cache = new MyCache<>();
        cache.map(map).evict(evict)
                .maxSize(size).load(load).persist(persist);
        cache.removeListeners(removeListeners);
        cache.init();
        return cache;
    }

}
