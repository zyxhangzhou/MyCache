package com.zyx.cacheCore.assistance.expire;

import cn.hutool.core.map.MapUtil;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheExpire;
import com.zyx.cacheApi.api.IMyCacheRemoveListener;
import com.zyx.cacheApi.api.IMyCacheRemoveListenerContext;
import com.zyx.cacheCore.assistance.listener.remove.MyCacheRemoveListenerContext;
import com.zyx.cacheCore.constant.enums.MyCacheRemoveType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/12 15:35
 * @Description 缓存过期的策略：随机获取
 */
@Slf4j
public class MyCacheExpireRandom<K, V> implements IMyCacheExpire<K, V> {

    /**
     * 单次清空的数量限制
     *
     * @since 0.0.16
     */
    private static final int COUNT_LIMIT = 100;

    /**
     * 过期 map
     * <p>
     * 空间换时间
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     */
    private final IMyCache<K, V> cache;

    /**
     * 是否启用快模式
     */
    private volatile boolean fastMode = false;

    /**
     * 线程执行类
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public MyCacheExpireRandom(IMyCache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            //1.判断是否为空
            if (MapUtil.isEmpty(expireMap)) {
                log.info("expireMap 信息为空，直接跳过本次处理。");
                return;
            }

            //2. 是否启用快模式
            if (fastMode) {
                expireKeys(10L);
            }

            //3. 缓慢模式
            expireKeys(100L);
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * 过期信息
     *
     * @param timeoutMills 超时时间
     */
    private void expireKeys(final long timeoutMills) {
        // 设置超时时间 100ms
        final long timeLimit = System.currentTimeMillis() + timeoutMills;
        // 恢复 fastMode
        this.fastMode = false;

        //2. 获取 key 进行处理
        int count = 0;
        while (true) {
            //2.1 返回判断
            if (count >= COUNT_LIMIT) {
                log.info("过期淘汰次数已经达到最大次数: {}，完成本次执行。", COUNT_LIMIT);
                return;
            }
            if (System.currentTimeMillis() >= timeLimit) {
                this.fastMode = true;
                log.info("过期淘汰已经达到限制时间，中断本次执行，设置 fastMode=true;");
                return;
            }

            //2.2 随机过期
            K key = getRandomKey();
            Long expireAt = expireMap.get(key);
            boolean expireFlag = expireKey(key, expireAt);
            log.debug("key: {} 过期执行结果 {}", key, expireFlag);

            //2.3 信息更新
            count++;
        }
    }

    /**
     * 过期处理 key
     *
     * @param key      key
     * @param expireAt 过期时间
     * @return 是否执行过期
     */
    private boolean expireKey(final K key, final Long expireAt) {
        if (expireAt == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
            expireMap.remove(key);
            // 再移除缓存，后续可以通过惰性删除做补偿
            V removeValue = cache.remove(key);

            // 执行淘汰监听器
            IMyCacheRemoveListenerContext<K, V> removeListenerContext =
                    MyCacheRemoveListenerContext.<K, V>newInstance()
                            .key(key).value(removeValue).type(MyCacheRemoveType.EXPIRE.code());
            cache.removeListeners().forEach(listener -> listener.listen(removeListenerContext));

            return true;
        }

        return false;
    }


    /**
     * 随机获取一个 key 信息
     * <p>
     * 将keySet转为List然后随机获得一个key
     *
     * @return 随机返回的 keys
     */
    private K getRandomKey() {
        Random random = ThreadLocalRandom.current();
        Set<K> keySet = expireMap.keySet();
        List<K> list = new ArrayList<>(keySet);
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollectionUtils.isEmpty(keyList)) return;
        // 判断大小，小的作为外循环。一般都是过期的 keys 比较小。
        if (keyList.size() <= expireMap.size()) {
            keyList.forEach(key -> expireKey(key, expireMap.get(key)));
        } else {
            expireMap.forEach(this::expireKey);
        }
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }
}
