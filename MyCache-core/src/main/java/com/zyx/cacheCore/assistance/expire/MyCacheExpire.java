package com.zyx.cacheCore.assistance.expire;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheExpire;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/6 21:04
 * @Description
 */
public class MyCacheExpire<K, V> implements IMyCacheExpire<K, V> {

    private static final int LIMIT = 120;

    //过期的map
    private final Map<K, Long> expireMap = new HashMap<>();

    private final IMyCache<K, V> cache;

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public MyCacheExpire(IMyCache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            if (MapUtil.isEmpty(expireMap)) return;
            int cnt = 0;
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                if (cnt >= LIMIT) return;
                expireKey(entry.getKey(), entry.getValue());
                ++cnt;
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

    private void expireKey(final K key, final Long expireAt) {
//        final K key = entry.getKey();
//        final Long expireAt = entry.getValue();
        // 删除的逻辑处理
        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
            expireMap.remove(key);
            // 再移除缓存，后续可以通过惰性删除做补偿
            cache.remove(key);
        }
    }


    // 很朴素的实现方式
    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollUtil.isEmpty(keyList)) return;
        // 判断大小，小的作为外循环。一般都是过期的 keys 比较小。
        if (keyList.size() <= expireMap.size()) {
            for (K key : keyList) {
                this.expireKey(key, expireMap.get(key));
            }
        } else {
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                this.expireKey(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }
}
