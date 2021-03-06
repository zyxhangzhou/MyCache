package com.zyx.cacheApi.annotation;

import java.lang.annotation.*;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 16:27
 * @Description 拦截器注解
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCacheInterceptor {
    /**
     * 通用拦截器
     *
     * 耗时统计 + 慢日志统计
     *
     * @return 默认开启
     */
    boolean common() default true;

    /**
     * 是否启用刷新
     * @return false
     */
    boolean refresh() default false;

    /**
     * 操作是否需要 append to file，默认为 false
     * 主要针对 cache 内容有变更的操作，不包括查询操作。
     * 包括删除，添加，过期等操作。
     * @return boolean
     */
    boolean aof() default false;

    /**
     * 是否执行驱除更新
     *
     * 主要用于 LRU/LFU 等驱除策略
     * @return boolean
     */
    boolean evict() default false;
}
