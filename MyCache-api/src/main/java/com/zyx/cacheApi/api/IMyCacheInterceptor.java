package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 17:28
 * @Description 拦截器接口
 */
public interface IMyCacheInterceptor<K, V> {

    /**
     * 方法执行之前
     *
     * @param context 上下文
     */
    void before(IMyCacheInterceptorContext<K, V> context);

    /**
     * 方法执行之后
     *
     * @param context 上下文
     */
    void after(IMyCacheInterceptorContext<K, V> context);
}
