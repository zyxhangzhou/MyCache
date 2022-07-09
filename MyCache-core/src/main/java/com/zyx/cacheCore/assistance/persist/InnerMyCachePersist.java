package com.zyx.cacheCore.assistance.persist;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCachePersist;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 19:51
 * @Description
 */
@Slf4j
public class InnerMyCachePersist<K, V> {
    private final IMyCache<K, V> cache;
    private final IMyCachePersist<K, V> persist;

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();


    public InnerMyCachePersist(IMyCache<K, V> cache, IMyCachePersist<K, V> persist) {
        this.cache = cache;
        this.persist = persist;
        init();
    }

    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            try {
                log.info("starting to persist");
                persist.persist(cache);
                log.info("persistence is done!");
            } catch (Exception exception) {
                log.error("file persistence cause an exception!", exception);
            }
        }, persist.delay(), persist.period(), persist.timeUnit());
    }
}
