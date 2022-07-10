package com.zyx.cacheCore.proxy.dynamic;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheProxy;
import com.zyx.cacheApi.api.IMyCacheProxyBsContext;
import com.zyx.cacheCore.proxy.bs.MyCacheProxyBs;
import com.zyx.cacheCore.proxy.bs.MyCacheProxyBsContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 13:45
 * @Description
 */
public class DynamicProxy implements InvocationHandler, IMyCacheProxy {

    private final IMyCache target;

    public DynamicProxy(IMyCache target) {
        this.target = target;
    }

    @Override
    public Object proxy() {
        // 我们要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法的
        InvocationHandler handler = new DynamicProxy(target);

        return Proxy.newProxyInstance(handler.getClass().getClassLoader(),
                target.getClass().getInterfaces(), handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        IMyCacheProxyBsContext context = MyCacheProxyBsContext.newInstance()
                .method(method).params(args).target(target);
        return MyCacheProxyBs.newInstance().context(context).execute();
    }
}
