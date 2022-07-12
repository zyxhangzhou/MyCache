package com.zyx.cacheCore.assistance.load;

import com.zyx.cacheApi.api.IMyCacheLoad;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 16:43
 * @Description 所有加载策略
 */
public final class MyCacheLoads {
    private MyCacheLoads() {
    }

    public static <K, V> IMyCacheLoad<K, V> none() {
        return new MyCacheLoadNone<>();
    }

    public static <K, V> IMyCacheLoad<K, V> json(final String dbPath) {
        return new MyCacheLoadJson<>(dbPath);
    }

    public static <K, V> IMyCacheLoad<K, V> aof(final String dbPath) {
        return new MyCacheLoadAof<>(dbPath);
    }
}
