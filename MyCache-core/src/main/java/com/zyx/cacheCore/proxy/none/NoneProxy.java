package com.zyx.cacheCore.proxy.none;

import com.zyx.cacheApi.api.IMyCacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 14:20
 * @Description
 */
public class NoneProxy implements InvocationHandler, IMyCacheProxy {

    private final Object target;

    public NoneProxy(Object target) {
        this.target = target;
    }

    /**
     * 返回原始对象，不去代理
     * @return 本来的对象
     */
    @Override
    public Object proxy() {
        return target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }
}
