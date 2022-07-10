package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 15:28
 * @Description
 */
public interface IMyCacheSlowListener {

    /**
     * 监听
     * @param context 上下文
     */
    void listen(final IMyCacheSlowListenerContext context);

    /**
     * 慢日志的阈值
     * @return long
     */
    long slowerThresholdMills();
}
