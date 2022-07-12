package com.zyx.cacheCore.core;

import com.zyx.cacheApi.annotation.MyCacheInterceptor;
import com.zyx.cacheApi.api.*;
import com.zyx.cacheCore.assistance.evict.MyCacheEvictContext;
import com.zyx.cacheCore.assistance.expire.MyCacheExpire;
import com.zyx.cacheCore.assistance.listener.remove.MyCacheRemoveListenerContext;
import com.zyx.cacheCore.assistance.listener.slow.MyCacheSlowListeners;
import com.zyx.cacheCore.assistance.persist.InnerMyCachePersist;
import com.zyx.cacheCore.constant.enums.MyCacheRemoveType;

import java.util.*;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/6/30 19:51
 */
public class MyCache<K, V> implements IMyCache<K, V> {

    /**
     * 存储的数据
     */
    private Map<K, V> map;

    /**
     * 大小限制
     */
    private int maxSize;

    private IMyCacheEvict<K, V> evict;

    private IMyCacheExpire<K, V> expire;

    private IMyCacheLoad<K,V> load;

    private IMyCachePersist<K,V> persist;

    private List<IMyCacheRemoveListener<K,V>> removeListeners;

    /**
     * 慢日志监听类
     */
    private List<IMyCacheSlowListener> slowListeners = MyCacheSlowListeners.none();

    public MyCache() {
    }

    public MyCache(MyCacheContext<K, V> context) {
        this.map = context.map();
        this.maxSize = context.size();
        this.evict = context.cacheEvict();
    }

    public MyCache<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    public MyCache<K, V> maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public MyCache<K, V> evict(IMyCacheEvict<K, V> cacheEvict) {
        this.evict = cacheEvict;
        return this;
    }

    public void persist(IMyCachePersist<K, V> persist) {
        this.persist = persist;
    }

    @Override
    @MyCacheInterceptor(refresh = true)
    public int size() {
        return map.size();
    }

    @Override
    @MyCacheInterceptor(refresh = true)
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @MyCacheInterceptor(refresh = true, evict = true)
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    @MyCacheInterceptor(refresh = true)
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    @MyCacheInterceptor(evict = true)
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        K key1 = (K) key;
        this.expire.refreshExpire(Collections.singletonList(key1));
        return map.get(key1);
    }

    @Override
    @MyCacheInterceptor(aof = true, evict = true)
    public V put(K key, V value) {
        MyCacheEvictContext<K, V> context = new MyCacheEvictContext<>();
        context.key(key).size(maxSize).cache(this);
        IMyCacheEntry<K, V> evictEntry = evict.evict(context);

        if (null != evictEntry) {
            IMyCacheRemoveListenerContext<K, V> removeListenerContext = MyCacheRemoveListenerContext.<K, V>newInstance()
                    .key(evictEntry.key())
                    .value(evictEntry.value())
                    .type(MyCacheRemoveType.EVICT.code());
            for (var listener: context.cache().removeListeners()) {
                listener.listen(removeListenerContext);
            }
        }

        return map.put(key, value);
    }

    @Override
    @MyCacheInterceptor(aof = true, evict = true)
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    @MyCacheInterceptor(aof = true)
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    @MyCacheInterceptor(aof = true, refresh = true)
    public void clear() {
        map.clear();
    }

    @Override
    @MyCacheInterceptor(refresh = true)
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    @MyCacheInterceptor(refresh = true)
    public Collection<V> values() {
        return map.values();
    }

    @Override
    @MyCacheInterceptor(refresh = true)
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    @MyCacheInterceptor
    public IMyCache<K, V> expire(K key, long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;
        this.expireAt(key, expireTime);
        return this;
    }

    @Override
    @MyCacheInterceptor
    public IMyCacheExpire<K, V> expire() {
        return this.expire;
    }

    @Override
    @MyCacheInterceptor(aof = true)
    public IMyCache<K, V> expireAt(K key, long timeInMills) {
        this.expire.expire(key, timeInMills);
        return this;
    }

    @Override
    public IMyCacheLoad<K, V> load() {
        return this.load;
    }

    @Override
    public IMyCachePersist<K, V> persist() {
        return this.persist;
    }

    @Override
    public IMyCacheEvict<K, V> evict() {
        return this.evict;
    }

    @Override
    public List<IMyCacheRemoveListener<K, V>> removeListeners() {
        return this.removeListeners;
    }

    @Override
    public List<IMyCacheSlowListener> slowListeners() {
        return this.slowListeners;
    }
    public MyCache<K, V> slowListeners(List<IMyCacheSlowListener> slowListeners) {
        this.slowListeners = slowListeners;
        return this;
    }

    public MyCache<K, V> removeListeners(List<IMyCacheRemoveListener<K, V>> removeListeners) {
        this.removeListeners = removeListeners;
        return this;
    }

    public MyCache<K, V> load(IMyCacheLoad<K, V> load) {
        this.load = load;
        return this;
    }

    public void init() {
        this.expire = new MyCacheExpire<>(this);
        this.load.load(this);

        if (null!=this.persist) {
            new InnerMyCachePersist<>(this, persist);
        }
    }
}
