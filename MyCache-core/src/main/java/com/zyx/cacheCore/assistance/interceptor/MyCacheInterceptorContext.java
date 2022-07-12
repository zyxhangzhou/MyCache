package com.zyx.cacheCore.assistance.interceptor;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheInterceptorContext;

import java.lang.reflect.Method;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 19:44
 * @Description 拦截器的上下文
 */
public class MyCacheInterceptorContext<K,V> implements IMyCacheInterceptorContext<K,V> {

    private IMyCache<K,V> cache;

    /**
     * 执行的方法信息
     */
    private Method method;

    /**
     * 执行的参数
     */
    private Object[] params;

    /**
     * 方法执行的结果
     */
    private Object result;

    /**
     * 开始时间
     */
    private long startMills;

    /**
     * 结束时间
     */
    private long endMills;

    public static <K,V> MyCacheInterceptorContext<K,V> newInstance() {
        return new MyCacheInterceptorContext<>();
    }

    @Override
    public IMyCache<K, V> cache() {
        return cache;
    }
    public MyCacheInterceptorContext<K, V> cache(IMyCache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public Method method() {
        return method;
    }

    public MyCacheInterceptorContext<K, V> method(Method method) {
        this.method = method;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public MyCacheInterceptorContext<K, V> params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public MyCacheInterceptorContext<K, V> result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public long startMills() {
        return startMills;
    }

    public MyCacheInterceptorContext<K, V> startMills(long startMills) {
        this.startMills = startMills;
        return this;
    }

    @Override
    public long endMills() {
        return endMills;
    }

    public MyCacheInterceptorContext<K, V> endMills(long endMills) {
        this.endMills = endMills;
        return this;
    }
}
