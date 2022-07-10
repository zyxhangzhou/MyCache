package com.zyx.cacheCore.proxy;

import cn.hutool.core.util.ObjectUtil;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheCore.proxy.cglib.CglibProxy;
import com.zyx.cacheCore.proxy.dynamic.DynamicProxy;
import com.zyx.cacheCore.proxy.none.NoneProxy;

import java.lang.reflect.Proxy;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 14:18
 * @Description
 */
public final class CacheProxy {
    private CacheProxy() {
    }

    public static <K, V> IMyCache<K, V> getProxy(final IMyCache<K, V> cache) {
        if (null == cache) {
            return (IMyCache<K, V>) new NoneProxy(cache).proxy();
        }

        final Class clazz = cache.getClass();

        // 如果targetClass本身是个接口或者targetClass是JDK Proxy生成的,则使用JDK动态代理。
        // 参考 spring 的 AOP 判断
        if (clazz.isInterface() || Proxy.isProxyClass(clazz)) {
            return (IMyCache<K, V>) new DynamicProxy(cache).proxy();
        }
        return (IMyCache<K, V>) new CglibProxy(cache).proxy();
    }
}
