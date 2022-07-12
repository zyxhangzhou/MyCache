package com.zyx.cacheCore.assistance.listener.slow;

import com.zyx.cacheApi.api.IMyCacheSlowListenerContext;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 15:55
 * @Description 删除监听类上下文
 */
public class MyCacheSlowListenerContext implements IMyCacheSlowListenerContext {

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数信息
     */
    private Object[] params;

    /**
     * 方法结果
     */
    private Object result;

    /**
     * 开始时间
     */
    private long startTimeMills;

    /**
     * 结束时间
     */
    private long endTimeMills;

    /**
     * 消耗时间
     */
    private long costTimeMills;

    private MyCacheSlowListenerContext() {
    }

    public static MyCacheSlowListenerContext newInstance() {
        return new MyCacheSlowListenerContext();
    }

    @Override
    public String methodName() {
        return methodName;
    }

    public MyCacheSlowListenerContext methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public MyCacheSlowListenerContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public MyCacheSlowListenerContext result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public long startTimeMills() {
        return startTimeMills;
    }

    public MyCacheSlowListenerContext startTimeMills(long startTimeMills) {
        this.startTimeMills = startTimeMills;
        return this;
    }

    @Override
    public long endTimeMills() {
        return endTimeMills;
    }

    public MyCacheSlowListenerContext endTimeMills(long endTimeMills) {
        this.endTimeMills = endTimeMills;
        return this;
    }

    @Override
    public long costTimeMills() {
        return costTimeMills;
    }

    public MyCacheSlowListenerContext costTimeMills(long costTimeMills) {
        this.costTimeMills = costTimeMills;
        return this;
    }
}
