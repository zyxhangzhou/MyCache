package com.zyx.cacheCore.core;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheCore.bootstrap.MyCacheBootstrap;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/5 16:19
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .size(3)
                .build();
        cache.put("a", "a");
        cache.put("b", "b");
        cache.put("c", "c");
        cache.put("d", "d");
        assert 3 == cache.size();
        System.out.println(cache.keySet());
    }
}
