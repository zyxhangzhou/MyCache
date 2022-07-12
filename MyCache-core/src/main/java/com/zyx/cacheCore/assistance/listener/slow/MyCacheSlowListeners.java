package com.zyx.cacheCore.assistance.listener.slow;

import com.zyx.cacheApi.api.IMyCacheSlowListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 16:24
 * @Description 所有的删除监听类实现
 */
public class MyCacheSlowListeners {
    private MyCacheSlowListeners(){}

    public static List<IMyCacheSlowListener> none() {
        return new ArrayList<>();
    }
    public static IMyCacheSlowListener defaults() {
        return new MyCacheSlowListener();
    }
}
