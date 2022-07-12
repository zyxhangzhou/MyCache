package com.zyx.cacheCore.assistance.listener.slow;

import com.zyx.cacheApi.api.IMyCacheSlowListenerContext;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 15:55
 * @Description
 */
public class MyCacheSlowListenerContext implements IMyCacheSlowListenerContext {
    private String methodName;
    private Object[] params;
    private Object result;
    private long startTimeMills;
    private long endTimeMills;
    private long costTimeMills;

    private MyCacheSlowListenerContext(){}

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
