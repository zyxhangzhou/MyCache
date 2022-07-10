package com.zyx.cacheCore.assistance.interceptor;

import com.zyx.cacheApi.api.IMyCacheInterceptor;
import com.zyx.cacheCore.assistance.interceptor.aof.MyCacheInterceptorAof;
import com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost;
import com.zyx.cacheCore.assistance.interceptor.evict.MyCacheInterceptorEvict;
import com.zyx.cacheCore.assistance.interceptor.refresh.MyCacheInterceptorRefresh;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 17:37
 * @Description
 */
public final class MyCacheInterceptors {

    /**
     * 默认通用
     * @return 结果
     */
    @SuppressWarnings("all")
    public static List<IMyCacheInterceptor> defaultCommonList() {
        List<IMyCacheInterceptor> list = new ArrayList<>();
        list.add(new MyCacheInterceptorCost());
        return list;
    }

    /**
     * 默认刷新
     * @return 结果
     * @since 0.0.5
     */
    @SuppressWarnings("all")
    public static List<IMyCacheInterceptor> defaultRefreshList() {
        List<IMyCacheInterceptor> list = new ArrayList<>();
        list.add(new MyCacheInterceptorRefresh());
        return list;
    }

    /**
     * 驱除策略拦截器
     * @return 结果
     */
    @SuppressWarnings("all")
    public static IMyCacheInterceptor evict() {
        return new MyCacheInterceptorEvict();
    }

    /**
     * AOF 模式
     * @return 结果
     * @since 0.0.10
     */
    @SuppressWarnings("all")
    public static IMyCacheInterceptor aof() {
        return new MyCacheInterceptorAof();
    }


}
