package com.zyx.cacheCore.assistance.load;

import com.zyx.cacheApi.api.IMyCacheLoad;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 16:43
 * @Description
 */
public final class MyCacheLoads {
    private MyCacheLoads() {}
    public static <K,V> IMyCacheLoad<K,V> none() {
        return new MyCacheLoadNone<>();
    }

}
