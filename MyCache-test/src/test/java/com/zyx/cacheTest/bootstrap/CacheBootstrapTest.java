package com.zyx.cacheTest.bootstrap;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheCore.assistance.evict.MyCacheEvicts;
import com.zyx.cacheCore.assistance.load.MyCacheLoads;
import com.zyx.cacheCore.assistance.persist.MyCachePersists;
import com.zyx.cacheCore.bootstrap.MyCacheBootstrap;
import com.zyx.cacheTest.listener.MyRemoveListener;
import com.zyx.cacheTest.listener.MySlowListener;
import com.zyx.cacheTest.load.MyCacheLoad;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 16:49
 * @Description 测试类
 */
public class CacheBootstrapTest {

    @Test
    public void MainTest() throws InterruptedException {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .size(3)
                .build();
        cache.put("a", "a");
        cache.put("b", "b");
        cache.expire("a", 10);
        Assert.assertEquals(2, cache.size());
        TimeUnit.MILLISECONDS.sleep(11);
        Assert.assertEquals(1, cache.size());
        System.out.println(cache.keySet());
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

    @Test
    public void loadJsonTest() {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .load(MyCacheLoads.json("./1.rdb"))
                .build();
        Assert.assertEquals(2, cache.size());
        System.out.println(cache.entrySet());
    }

    @Test
    public void removeListenerTest() {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .size(2)
                .addRemoveListener(new MyRemoveListener<>())
                .build();
        cache.put("math", "98");
        cache.put("reading", "90");
        cache.put("writing", "94");
    }

    @Test
    public void slowListenerTest() {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .addSlowListener(new MySlowListener())
                .build();
        cache.put("1", "2");
        cache.put("3", "2");
        System.out.println(cache.entrySet());
    }

    @Test
    public void persistAofTest() throws InterruptedException {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .persist(MyCachePersists.json("1.rdb"))
                //.persist(MyCachePersists.aof("1.aof"))
                .build();

        cache.put("x", "x");
        cache.expire("x", 10);
        cache.remove("2");

        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void loadAofTest() {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .load(MyCacheLoads.aof("default.aof"))
                .build();

        Assert.assertEquals(1, cache.size());
        System.out.println(cache.entrySet());
    }

    @Test
    public void lruTest() {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .size(3)
                .evict(MyCacheEvicts.lru())
                .build();
        cache.put("A", "hi");
        cache.put("B", "cpp");
        cache.put("C", "java");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");
        Assert.assertEquals(3, cache.size());

        System.out.println(cache.keySet());
    }

    @Test
    public void lruTest2() {
        IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .size(3)
                .evict(MyCacheEvicts.clock())
                .build();
        cache.put("A", "hi");
        cache.put("B", "cpp");
        cache.put("C", "java");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");
        Assert.assertEquals(3, cache.size());

        System.out.println(cache.keySet());
    }

    @Test
    public void commonTest() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {

            if (entry.getKey() == 3) {
                map.remove(entry.getKey());
            }

            System.out.println(map.entrySet());
        }
        System.out.println("==============");
        System.out.println(map);
    }
}

