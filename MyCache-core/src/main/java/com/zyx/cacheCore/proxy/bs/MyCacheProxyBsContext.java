package com.zyx.cacheCore.proxy.bs;

import com.zyx.cacheApi.annotation.MyCacheInterceptor;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheProxyBsContext;

import java.lang.reflect.Method;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 13:24
 * @Description
 */
public class MyCacheProxyBsContext implements IMyCacheProxyBsContext {

    /**
     * 目标
     */
    private IMyCache target;

    /**
     * 入参
     */
    private Object[] params;

    /**
     * 方法
     */
    private Method method;

    /**
     * 拦截器
     */
    private MyCacheInterceptor interceptor;

    public static MyCacheProxyBsContext newInstance() {
        return new MyCacheProxyBsContext();
    }

    @Override
    public MyCacheInterceptor interceptor() {
        return interceptor;
    }

    @Override
    public IMyCache target() {
        return target;
    }

    @Override
    public IMyCacheProxyBsContext target(IMyCache target) {
        this.target = target;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public MyCacheProxyBsContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Method method() {
        return method;
    }

    public MyCacheProxyBsContext method(Method method) {
        this.method = method;
        this.interceptor = method.getAnnotation(MyCacheInterceptor.class);
        return this;
    }

    @Override
    public Object process() throws Throwable {
        return this.method.invoke(target, params);
    }


}
