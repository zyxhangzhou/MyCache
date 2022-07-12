package com.zyx.cacheCore.proxy.bs;

import com.zyx.cacheApi.annotation.MyCacheInterceptor;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheInterceptor;
import com.zyx.cacheApi.api.IMyCachePersist;
import com.zyx.cacheApi.api.IMyCacheProxyBsContext;
import com.zyx.cacheCore.assistance.interceptor.MyCacheInterceptorContext;
import com.zyx.cacheCore.assistance.interceptor.MyCacheInterceptors;
import com.zyx.cacheCore.assistance.persist.MyCachePersistAof;

import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 19:37
 * @Description 代理引导类
 */
public class MyCacheProxyBs {
    private MyCacheProxyBs() {
    }

    /**
     * 代理上下文
     */
    private IMyCacheProxyBsContext context;

    /**
     * 默认通用拦截器
     * <p>
     * JDK 的泛型擦除导致这里不能使用泛型
     */
    @SuppressWarnings("all")
    private final List<IMyCacheInterceptor> commonInterceptors = MyCacheInterceptors.defaultCommonList();

    /**
     * 默认刷新拦截器
     */
    @SuppressWarnings("all")
    private final List<IMyCacheInterceptor> refreshInterceptors = MyCacheInterceptors.defaultRefreshList();

    /**
     * 持久化拦截器
     */
    @SuppressWarnings("all")
    private final IMyCacheInterceptor persistInterceptors = MyCacheInterceptors.aof();

    /**
     * 驱除拦截器
     */
    @SuppressWarnings("all")
    private final IMyCacheInterceptor evictInterceptors = MyCacheInterceptors.evict();

    /**
     * 新建对象实例
     *
     * @return 实例
     */
    public static MyCacheProxyBs newInstance() {
        return new MyCacheProxyBs();
    }

    public MyCacheProxyBs context(IMyCacheProxyBsContext context) {
        this.context = context;
        return this;
    }

    /**
     * 执行
     *
     * @return 结果
     * @throws Throwable 异常
     */
    @SuppressWarnings("all")
    public Object execute() throws Throwable {
        //1. 开始的时间
        final long startMills = System.currentTimeMillis();
        final IMyCache cache = context.target();
        MyCacheInterceptorContext interceptorContext = MyCacheInterceptorContext.newInstance()
                .startMills(startMills)
                .method(context.method())
                .params(context.params())
                .cache(context.target());

        //1. 获取刷新注解信息
        MyCacheInterceptor cacheInterceptor = context.interceptor();
        this.interceptorHandler(cacheInterceptor, interceptorContext, cache, true);

        //2. 正常执行
        Object result = context.process();

        final long endMills = System.currentTimeMillis();
        interceptorContext.endMills(endMills).result(result);

        // 方法执行完成
        this.interceptorHandler(cacheInterceptor, interceptorContext, cache, false);
        return result;
    }

    /**
     * 拦截器执行类
     *
     * @param cacheInterceptor   缓存拦截器
     * @param interceptorContext 上下文
     * @param cache              缓存
     * @param before             是否执行执行
     */
    @SuppressWarnings("all")
    private void interceptorHandler(MyCacheInterceptor cacheInterceptor,
                                    MyCacheInterceptorContext interceptorContext,
                                    IMyCache cache,
                                    boolean before) {
        if (cacheInterceptor == null) return;
        //1. 通用
        if (cacheInterceptor.common()) {
            commonInterceptors.forEach(interceptor -> {
                if (before) interceptor.before(interceptorContext);
                else interceptor.after(interceptorContext);
            });
        }

        //2. 刷新
        if (cacheInterceptor.refresh()) {
            refreshInterceptors.forEach(interceptor -> {
                if (before) interceptor.before(interceptorContext);
                else interceptor.after(interceptorContext);
            });
        }

        //3. AOF 追加
        //当持久化类为AOF模式时，才进行调用
        final IMyCachePersist cachePersist = cache.persist();
        if (cacheInterceptor.aof() && (cachePersist instanceof MyCachePersistAof)) {
            if (before) {
                persistInterceptors.before(interceptorContext);
            } else {
                persistInterceptors.after(interceptorContext);
            }
        }

        //4. 驱除策略更新
        if (cacheInterceptor.evict()) {
            if (before) {
                evictInterceptors.before(interceptorContext);
            } else {
                evictInterceptors.after(interceptorContext);
            }
        }
    }
}
