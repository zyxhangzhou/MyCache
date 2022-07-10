package com.zyx.cacheCore.assistance.interceptor.refresh;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheInterceptor;
import com.zyx.cacheApi.api.IMyCacheInterceptorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 19:41
 * @Description
 */
@Slf4j
public class MyCacheInterceptorRefresh<K,V> implements IMyCacheInterceptor<K, V> {
    @Override
    public void before(IMyCacheInterceptorContext<K, V> context) {
        log.info("Refresh start");
        final IMyCache<K,V> cache = context.cache();
        cache.expire().refreshExpire(cache.keySet());
    }

    @Override
    public void after(IMyCacheInterceptorContext<K, V> context) {

    }
}
