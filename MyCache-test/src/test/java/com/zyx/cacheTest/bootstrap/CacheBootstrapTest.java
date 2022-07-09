package com.zyx.cacheTest.bootstrap;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCachePersist;
import com.zyx.cacheCore.assistance.persist.MyCachePersists;
import com.zyx.cacheCore.bootstrap.MyCacheBootstrap;
import com.zyx.cacheTest.load.MyCacheLoad;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 16:49
 * @Description
 */
public class CacheBootstrapTest {

    @Test
    public void MainTest() {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .size(3)
                .build();
        cache.put("a", "a");
        cache.put("b", "b");
        cache.put("c", "c");
        cache.put("d", "d");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.entrySet());
    }

    @Test
    public void loadTest() {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .load(new MyCacheLoad())
                .build();

        Assert.assertEquals(2, cache.size());
        System.out.println(cache.entrySet());
    }

    @Test
    public void persistTest() throws InterruptedException {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .load(new MyCacheLoad())
                .persist(MyCachePersists.json("./1.rdb"))
                .build();

        Assert.assertEquals(2, cache.size());
        System.out.println(cache.entrySet());
        TimeUnit.SECONDS.sleep(5);
    }
}

