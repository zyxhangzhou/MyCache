package com.zyx.cacheApi.api;

import com.zyx.cacheApi.annotation.MyCacheInterceptor;

import java.lang.reflect.Method;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 19:38
 * @Description 代理类的上下文
 */
public interface IMyCacheProxyBsContext {

    /**
     * 拦截器信息
     *
     * @return 拦截器
     */
    MyCacheInterceptor interceptor();

    /**
     * 获取代理对象信息
     *
     * @return 代理
     */
    IMyCache target();

    /**
     * 目标对象
     *
     * @param target 对象
     * @return 结果
     */
    IMyCacheProxyBsContext target(final IMyCache target);

    /**
     * 参数信息
     *
     * @return 参数信息
     */
    Object[] params();

    /**
     * 方法信息
     *
     * @return 方法信息
     */
    Method method();

    /**
     * 方法执行
     *
     * @return 执行
     * @throws Throwable 异常信息
     */
    Object process() throws Throwable;
}
