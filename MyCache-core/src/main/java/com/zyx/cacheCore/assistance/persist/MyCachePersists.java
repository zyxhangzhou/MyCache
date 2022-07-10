package com.zyx.cacheCore.assistance.persist;

import com.zyx.cacheApi.api.IMyCachePersist;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 20:11
 * @Description
 */
public class MyCachePersists {
    private MyCachePersists() {
    }

    public static <K, V> IMyCachePersist<K, V> none() {
        return new MyCachePersistNone<>();
    }

    public static <K, V> IMyCachePersist<K, V> json(final String path) {
        return new MyCachePersistJson<>(path);
    }

    /**
     * AOF 持久化
     *
     * @param path 路径
     * @param <K>  key
     * @param <V>  value
     * @return MyCachePersistAof
     */
    public static <K, V> IMyCachePersist<K, V> aof(final String path) {
        return new MyCachePersistAof<>(path);
    }
}
