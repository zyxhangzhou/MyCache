package com.zyx.cacheCore.assistance.listener.remove;

import com.zyx.cacheApi.api.IMyCacheRemoveListenerContext;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 14:45
 * @Description 删除的监听类上下文
 */
public class MyCacheRemoveListenerContext<K, V> implements IMyCacheRemoveListenerContext<K, V> {

    private K key;

    private V value;

    /**
     * 删除的类型
     * ① evict
     * ② expire
     */
    private String type;

    private MyCacheRemoveListenerContext() {}

    public static <K,V> MyCacheRemoveListenerContext<K,V> newInstance() {
        return new MyCacheRemoveListenerContext<>();
    }

    @Override
    public K key() {
        return key;
    }

    public MyCacheRemoveListenerContext<K, V> key(K key) {
        this.key = key;
        return this;
    }

    @Override
    public V value() {
        return value;
    }

    public MyCacheRemoveListenerContext<K, V> value(V value) {
        this.value = value;
        return this;
    }

    @Override
    public String type() {
        return type;
    }

    public MyCacheRemoveListenerContext<K, V> type(String type) {
        this.type = type;
        return this;
    }
}
