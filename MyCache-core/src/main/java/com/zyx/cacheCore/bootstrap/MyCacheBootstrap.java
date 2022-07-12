package com.zyx.cacheCore.bootstrap;

import com.zyx.cacheApi.api.*;
import com.zyx.cacheCore.assistance.evict.MyCacheEvicts;
import com.zyx.cacheCore.assistance.listener.remove.MyCacheRemoveListeners;
import com.zyx.cacheCore.assistance.listener.slow.MyCacheSlowListeners;
import com.zyx.cacheCore.assistance.load.MyCacheLoads;
import com.zyx.cacheCore.assistance.persist.MyCachePersists;
import com.zyx.cacheCore.assistance.util.ArgumentUtils;
import com.zyx.cacheCore.core.MyCache;
import com.zyx.cacheCore.proxy.CacheProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 16:58
 * @Description 缓存引导类
 */
public class MyCacheBootstrap<K, V> {
    private MyCacheBootstrap() {
    }

    /**
     * 创建对象实例
     *
     * @param <K> key
     * @param <V> value
     * @return this
     */
    public static <K, V> MyCacheBootstrap<K, V> newInstance() {
        return new MyCacheBootstrap<>();
    }

    /**
     * map 实现
     */
    private Map<K, V> map = new HashMap<>();

    /**
     * 大小限制
     */
    private int size = Integer.MAX_VALUE;

    /**
     * 驱除策略-默认FIFO
     */
    private IMyCacheEvict<K, V> evict = MyCacheEvicts.fifo();

    /**
     * 删除监听类
     */
    private final List<IMyCacheRemoveListener<K, V>> removeListeners = MyCacheRemoveListeners.defaults();

    /**
     * 慢操作监听类
     */
    private final List<IMyCacheSlowListener> slowListeners = MyCacheSlowListeners.none();
    private IMyCacheLoad<K, V> load = MyCacheLoads.none();

    /**
     * 持久化策略
     */
    private IMyCachePersist<K, V> persist = MyCachePersists.none();

    public MyCacheBootstrap<K, V> map(Map<K, V> map) {
        Objects.requireNonNull(map, "map is null!");
        this.map = map;
        return this;
    }

    public MyCacheBootstrap<K, V> size(int size) {
        ArgumentUtils.requireNonNegative(size, "size");
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

    public MyCacheBootstrap<K, V> addSlowListener(IMyCacheSlowListener slowListener) {
        ArgumentUtils.requireNotNull(slowListener, "slowListener");
        this.slowListeners.add(slowListener);
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
        cache.removeListeners(removeListeners).slowListeners(slowListeners);
        cache.init();
        return CacheProxy.getProxy(cache);
    }

}
