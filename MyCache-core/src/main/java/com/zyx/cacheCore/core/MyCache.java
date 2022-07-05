package com.zyx.cacheCore.core;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheEvict;
import com.zyx.cacheCore.assistance.evict.MyCacheEvictContext;

import java.util.Collection;
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
        return map.get(key1);
    }

    @Override
    public V put(K key, V value) {
        MyCacheEvictContext<K, V> context = new MyCacheEvictContext<>();
        context.setKey(key).setSize(maxSize).setCache(this);
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
}
