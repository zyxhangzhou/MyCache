package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 13:33
 * @Description 删除监听器接口
 */
public interface IMyCacheRemoveListener<K, V> {

    /**
     * 监听
     *
     * @param context 上下文
     */
    void listen(final IMyCacheRemoveListenerContext<K, V> context);
}
