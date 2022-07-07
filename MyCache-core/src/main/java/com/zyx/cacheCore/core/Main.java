package com.zyx.cacheCore.core;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheCore.bootstrap.MyCacheBootstrap;

import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/5 16:19
 * @Description
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .size(3)
                .build();
        cache.put("a", "a");
        cache.put("b", "b");
        cache.put("c", "c");
        cache.put("d", "d");
        assert 3 == cache.size();
        System.out.println(cache.keySet());
        cache.expire("d", 10);
        assert 3 == cache.size();
        TimeUnit.MILLISECONDS.sleep(109);
        System.out.println(cache.size());
        System.out.println(cache.keySet());
    }
}
