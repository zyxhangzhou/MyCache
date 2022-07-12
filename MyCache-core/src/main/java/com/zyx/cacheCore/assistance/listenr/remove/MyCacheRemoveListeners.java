package com.zyx.cacheCore.assistance.listenr.remove;

import com.zyx.cacheApi.api.IMyCacheRemoveListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 14:13
 * @Description
 */
public class MyCacheRemoveListeners {
    private MyCacheRemoveListeners() {
    }

    public static <K, V> List<IMyCacheRemoveListener<K, V>> defaults() {
        List<IMyCacheRemoveListener<K, V>> listeners = new ArrayList<>();
        listeners.add(new MyCacheRemoveListener());
        return listeners;
    }
}
