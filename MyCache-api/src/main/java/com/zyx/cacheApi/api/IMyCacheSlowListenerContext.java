package com.zyx.cacheApi.api;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 15:29
 * @Description
 */
public interface IMyCacheSlowListenerContext {

    /**
     * 方法名
     * @return 方法名
     */
    String methodName();

    /**
     * 参数信息
     * @return 参数数组
     */
    Object[] params();

    /**
     * 结果
     * @return Object
     */
    Object result();

    /**
     * 开始时间
     * @return long
     */
    long startTimeMills();

    /**
     * 结束时间
     * @return long
     */
    long endTimeMills();

    /**
     * 总耗时
     * @return long
     */
    long costTimeMills();
}
