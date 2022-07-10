package com.zyx.cacheCore.proxy.cglib;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheProxy;
import com.zyx.cacheApi.api.IMyCacheProxyBsContext;
import com.zyx.cacheCore.proxy.bs.MyCacheProxyBs;
import com.zyx.cacheCore.proxy.bs.MyCacheProxyBsContext;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 13:31
 * @Description Cglib代理类
 */
public class CglibProxy implements IMyCacheProxy, MethodInterceptor {

    private final IMyCache target;

    public CglibProxy(IMyCache target) {
        this.target = target;
    }

    @Override
    public Object proxy() {
        Enhancer enhancer = new Enhancer();
        //目标类
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        //使用字节码技术创建目标对象类的子类实例作为代理
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        IMyCacheProxyBsContext context = MyCacheProxyBsContext.newInstance()
                .method(method).params(params).target(target);
        return MyCacheProxyBs.newInstance().context(context).execute();
    }
}
