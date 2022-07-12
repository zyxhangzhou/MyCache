package com.zyx.cacheCore.assistance.listener.remove;

import com.zyx.cacheApi.api.IMyCacheRemoveListener;
import com.zyx.cacheApi.api.IMyCacheRemoveListenerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 13:52
 * @Description
 */
@Slf4j
public class MyCacheRemoveListener<K, V> implements IMyCacheRemoveListener<K, V> {
    @Override
    public void listen(IMyCacheRemoveListenerContext<K, V> context) {
        log.info("Removed key: {}, value: {}, type: {}",
                context.key(), context.value(), context.type());
    }
}
