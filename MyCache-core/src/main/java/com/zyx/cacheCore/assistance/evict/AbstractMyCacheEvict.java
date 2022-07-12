package com.zyx.cacheCore.assistance.evict;

import com.zyx.cacheApi.api.IMyCacheEntry;
import com.zyx.cacheApi.api.IMyCacheEvict;
import com.zyx.cacheApi.api.IMyCacheEvictContext;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/11 14:18
 * @Description 驱逐策略-抽象类
 */
public abstract class AbstractMyCacheEvict<K, V> implements IMyCacheEvict<K, V> {

    /**
     * 实现doEvict方法
     *
     * @param context 上下文
     * @return IMyCacheEntry
     */
    @Override
    public IMyCacheEntry<K, V> evict(IMyCacheEvictContext<K, V> context) {
        return doEvict(context);
    }

    /**
     * 执行移除操作
     *
     * @param context 上下文
     * @return IMyCacheEntry
     */
    protected abstract IMyCacheEntry<K, V> doEvict(IMyCacheEvictContext<K, V> context);


    /**
     * 更新
     *
     * @param key key
     */
    @Override
    public void updateKey(K key) {

    }

    /**
     * 删除
     *
     * @param key key
     */
    @Override
    public void removeKey(K key) {

    }
}
