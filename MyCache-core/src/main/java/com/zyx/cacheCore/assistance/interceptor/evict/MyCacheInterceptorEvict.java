package com.zyx.cacheCore.assistance.interceptor.evict;

import com.zyx.cacheApi.api.IMyCacheEvict;
import com.zyx.cacheApi.api.IMyCacheInterceptor;
import com.zyx.cacheApi.api.IMyCacheInterceptorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 17:40
 * @Description
 */
@Slf4j
public class MyCacheInterceptorEvict<K,V> implements IMyCacheInterceptor<K, V> {
    @Override
    public void before(IMyCacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(IMyCacheInterceptorContext<K, V> context) {
        IMyCacheEvict<K,V> evict = context.cache().evict();

        Method method = context.method();
        final K key = (K) context.params()[0];
        if("remove".equals(method.getName())) {
            evict.removeKey(key);
        } else {
            evict.updateKey(key);
        }
    }
}
