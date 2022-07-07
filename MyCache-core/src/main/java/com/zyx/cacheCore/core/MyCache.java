package com.zyx.cacheCore.core;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEvict;
import com.zyx.cacheApi.api.IMyCacheExpire;
import com.zyx.cacheCore.assistance.evict.MyCacheEvictContext;
import com.zyx.cacheCore.assistance.expire.MyCacheExpire;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

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

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        K key1 = (K) key;
        this.expire.refreshExpire(Collections.singletonList(key1));
        return map.get(key1);
    }

    @Override
    public V put(K key, V value) {
        MyCacheEvictContext<K, V> context = new MyCacheEvictContext<>();
        context.key(key).size(maxSize).cache(this);
        evict.evict(context);
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public IMyCache<K, V> expire(K key, long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;
        this.expireAt(key, expireTime);
        return this;
    }

    @Override
    public IMyCache<K, V> expireAt(K key, long timeInMills) {
        this.expire.expire(key, timeInMills);
        return this;
    }

    public void init() {
        this.expire = new MyCacheExpire<>(this);
    }
}
