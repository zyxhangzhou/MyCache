package com.zyx.cacheCore.assistance.listenr.slow;

import com.zyx.cacheApi.api.IMyCacheSlowListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 16:24
 * @Description
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
