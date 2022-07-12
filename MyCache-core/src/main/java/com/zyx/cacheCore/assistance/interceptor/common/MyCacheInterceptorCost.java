package com.zyx.cacheCore.assistance.interceptor.common;

import com.zyx.cacheApi.api.IMyCacheInterceptor;
import com.zyx.cacheApi.api.IMyCacheInterceptorContext;
import com.zyx.cacheApi.api.IMyCacheSlowListener;
import com.zyx.cacheCore.assistance.listener.slow.MyCacheSlowListenerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 17:23
 * @Description
 */
@Slf4j
public class MyCacheInterceptorCost<K, V> implements IMyCacheInterceptor<K, V> {
    @Override
    public void before(IMyCacheInterceptorContext<K, V> context) {
        log.debug("Cost start, method: {}", context.method().getName());
    }

    @Override
    public void after(IMyCacheInterceptorContext<K, V> context) {
        long costMills = context.endMills()-context.startMills();
        final String methodName = context.method().getName();
        log.debug("Cost end, method: {}, cost: {}ms", methodName, costMills);

        // 添加慢日志操作
        List<IMyCacheSlowListener> slowListeners = context.cache().slowListeners();
        if(CollectionUtils.isNotEmpty(slowListeners)) {
            MyCacheSlowListenerContext listenerContext = MyCacheSlowListenerContext.newInstance().startTimeMills(context.startMills())
                    .endTimeMills(context.endMills())
                    .costTimeMills(costMills)
                    .methodName(methodName)
                    .params(context.params())
                    .result(context.result());

            // 设置多个，可以考虑不同的慢日志级别，做不同的处理
            slowListeners.forEach(slowListener -> {
                if (costMills >= slowListener.slowerThresholdMills())
                    slowListener.listen(listenerContext);
            });
        }
    }
}
