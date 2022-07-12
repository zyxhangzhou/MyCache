package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 13:32
 * @Description 缓存代理接口
 */
public interface IMyCacheProxy {

    /**
     * 获取代理实现
     *
     * @return 代理
     */
    Object proxy();
}
