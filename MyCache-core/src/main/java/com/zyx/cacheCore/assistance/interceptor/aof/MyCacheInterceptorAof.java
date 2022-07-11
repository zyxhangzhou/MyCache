package com.zyx.cacheCore.assistance.interceptor.aof;

import com.alibaba.fastjson.JSON;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheInterceptor;
import com.zyx.cacheApi.api.IMyCacheInterceptorContext;
import com.zyx.cacheApi.api.IMyCachePersist;
import com.zyx.cacheCore.assistance.persist.MyCachePersistAof;
import com.zyx.cacheCore.model.PersistAofEntry;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 17:46
 * @Description
 */
@Slf4j
public class MyCacheInterceptorAof<K, V> implements IMyCacheInterceptor<K, V> {
    @Override
    public void before(IMyCacheInterceptorContext<K, V> context) {
        //nothing to do
    }

    @Override
    public void after(IMyCacheInterceptorContext<K, V> context) {
        IMyCache<K, V> cache = context.cache();
        IMyCachePersist<K, V> persist = cache.persist();

        if (persist instanceof MyCachePersistAof) {
            MyCachePersistAof<K, V> cachePersistAof = (MyCachePersistAof<K, V>) persist;

            String methodName = context.method().getName();
            PersistAofEntry aofEntry = PersistAofEntry.newInstance();
            aofEntry.setMethodName(methodName);
            aofEntry.setParams(context.params());

            String json = JSON.toJSONString(aofEntry);

            // 直接持久化
            log.debug("AOF Starting to append： {}", json);
            cachePersistAof.append(json);
            log.debug("AOF Appending is done!： {}", json);
        }
    }
}
