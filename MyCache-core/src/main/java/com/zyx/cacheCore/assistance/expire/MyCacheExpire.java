package com.zyx.cacheCore.assistance.expire;

import cn.hutool.core.map.MapUtil;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheExpire;
import com.zyx.cacheApi.api.IMyCacheRemoveListenerContext;
import com.zyx.cacheCore.assistance.listener.remove.MyCacheRemoveListenerContext;
import com.zyx.cacheCore.constant.enums.MyCacheRemoveType;
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
 * @Description 缓存过期策略
 */
public class MyCacheExpire<K, V> implements IMyCacheExpire<K, V> {

    /**
     * 单次清空的数量限制
     */
    private static final int LIMIT = 120;

    /**
     * 过期的map
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     */
    private final IMyCache<K, V> cache;

    /**
     * 定时任务
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public MyCacheExpire(IMyCache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    /**
     * 初始化任务
     */
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
        if (expireAt == null) return;
        // 删除的逻辑处理
        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
            expireMap.remove(key);
            // 再移除缓存，后续可以通过惰性删除做补偿
            V removedValue = cache.remove(key);

            IMyCacheRemoveListenerContext<K, V> context = MyCacheRemoveListenerContext.<K, V>newInstance()
                    .key(key).value(removedValue)
                    .type(MyCacheRemoveType.EXPIRE.code());
            cache.removeListeners().forEach(listener -> listener.listen(context));
        }
    }


    // 很朴素的实现方式
    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollectionUtils.isEmpty(keyList)) return;
        // 判断大小，小的作为外循环。一般都是过期的 keys 比较小。
        if (keyList.size() <= expireMap.size()) {
            for(K key : keyList) {
                Long expireAt = expireMap.get(key);
                expireKey(key, expireAt);
            }
        } else {
            for(var entry : expireMap.entrySet()) {
                this.expireKey(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }
}
