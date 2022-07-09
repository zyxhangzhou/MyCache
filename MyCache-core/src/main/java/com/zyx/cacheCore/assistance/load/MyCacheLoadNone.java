package com.zyx.cacheCore.assistance.load;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheLoad;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 16:44
 * @Description
 */
public class MyCacheLoadNone<K,V> implements IMyCacheLoad<K,V> {
    @Override
    public void load(IMyCache<K, V> cache) {
    }
}
