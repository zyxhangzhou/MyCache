package com.zyx.cacheTest.listenr;

import com.zyx.cacheApi.api.IMyCacheRemoveListener;
import com.zyx.cacheApi.api.IMyCacheRemoveListenerContext;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 14:09
 * @Description
 */
public class MyRemoveListener<K, V> implements IMyCacheRemoveListener<K, V> {
    @Override
    public void listen(IMyCacheRemoveListenerContext<K, V> context) {
        System.out.printf("【删除警告】Oops，竟然被删除了！ key: %s, value: %s\n",
                context.key().toString(), context.value().toString());
    }
}
