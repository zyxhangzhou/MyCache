package com.zyx.cacheCore.assistance.expire;

import cn.hutool.core.map.MapUtil;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheExpire;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 12:54
 * @Description
 */
public class MyCacheExpireSort<K, V> implements IMyCacheExpire<K, V> {

    /**
     * 单次清空的数量最大限制
     */
    private static final int LIMIT = 100;

    /**
     * 排序缓存存储
     * <p>
     * 使用按照时间排序的缓存处理。
     */
    private final Map<Long, List<K>> sortMap = new TreeMap<>((o1, o2) -> (int) (o1 - o2));

    /**
     * 过期 map
     * <p>
     * 空间换时间
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    private final IMyCache<K, V> cache;

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();


    public MyCacheExpireSort(IMyCache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            //1.判断是否为空
            if (MapUtil.isEmpty(sortMap)) return;

            //2. 获取 key 进行处理
            int count = 0;
            var entryIterator = sortMap.entrySet().iterator();
            while (entryIterator.hasNext()) {
                var entry = entryIterator.next();
                final Long expireAt = entry.getKey();
                List<K> expireKeys = entry.getValue();

                // 判断队列是否为空
                if (CollectionUtils.isEmpty(expireKeys)) {
                    entryIterator.remove();
                    continue;
                }
                if (count >= LIMIT) {
                    return;
                }

                // 删除的逻辑处理
                long currentTime = System.currentTimeMillis();
                if (currentTime >= expireAt) {
                    Iterator<K> iterator = expireKeys.iterator();
                    while (iterator.hasNext()) {
                        K key = iterator.next();
                        // 先移除本身
                        iterator.remove();
                        expireMap.remove(key);

                        // 再移除缓存，后续可以通过惰性删除做补偿
                        cache.remove(key);
                        count++;
                    }
                } else {
                    // 直接跳过，没有过期的信息
                    return;
                }
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void expire(K key, long expireAt) {
        List<K> keys = sortMap.get(expireAt);
        if (keys == null) {
            keys = new ArrayList<>();
        }
        keys.add(key);

        // 设置对应的信息
        sortMap.put(expireAt, keys);
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollectionUtils.isEmpty(keyList)) return;
        // 这样维护两套的代价太大，后续优化，暂时不用。
        // 判断大小，小的作为外循环
        final int expireSize = expireMap.size();
        if (expireSize <= keyList.size()) {
            // 一般过期的数量都是较少的
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                K key = entry.getKey();

                // 这里直接执行过期处理，不再判断是否存在于集合中。
                // 因为基于集合的判断，时间复杂度为 O(n)
                this.removeExpireKey(key);
            }
        } else {
            for (K key : keyList) {
                this.removeExpireKey(key);
            }
        }

    }

    /**
     * 移除过期信息
     *
     * @param key key
     */
    private void removeExpireKey(final K key) {
        Long expireTime = expireMap.get(key);
        if (expireTime != null) {
            final long currentTime = System.currentTimeMillis();
            if (currentTime >= expireTime) {
                expireMap.remove(key);

                List<K> expireKeys = sortMap.get(expireTime);
                expireKeys.remove(key);
                sortMap.put(expireTime, expireKeys);
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }
}
