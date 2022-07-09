package com.zyx.cacheCore.model;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 17:47
 * @Description 持久化的存储对
 */
public class PersistRdbEntry<K, V> {
    public PersistRdbEntry() {
    }

    private K key;
    private V value;
    private Long expire;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
