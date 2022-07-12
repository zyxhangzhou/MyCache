package com.zyx.cacheCore.model;

import java.util.Arrays;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 16:50
 * @Description AOF持久化的节点信息
 */
public class PersistAofEntry {

    /**
     * 参数信息
     */
    private Object[] params;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 新建对象实例
     *
     * @return this
     */
    public static PersistAofEntry newInstance() {
        return new PersistAofEntry();
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
