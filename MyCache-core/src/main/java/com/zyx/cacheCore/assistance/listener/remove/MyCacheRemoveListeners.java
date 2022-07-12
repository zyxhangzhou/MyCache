package com.zyx.cacheCore.assistance.listener.remove;

import com.zyx.cacheApi.api.IMyCacheRemoveListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 14:13
 * @Description 所有的删除监听类
 */
public class MyCacheRemoveListeners {
    private MyCacheRemoveListeners() {
    }

    /**
     * 默认监听类
     *
     * @param <K> key
     * @param <V> value
     * @return 监听类列表
     */
    @SuppressWarnings("all")
    public static <K, V> List<IMyCacheRemoveListener<K, V>> defaults() {
        List<IMyCacheRemoveListener<K, V>> listeners = new ArrayList<>();
        listeners.add(new MyCacheRemoveListener());
        return listeners;
    }
}
