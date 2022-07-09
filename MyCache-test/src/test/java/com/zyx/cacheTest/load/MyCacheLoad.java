package com.zyx.cacheTest.load;

import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheLoad;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 16:34
 * @Description
 */
public class MyCacheLoad implements IMyCacheLoad<String ,String> {
    @Override
    public void load(IMyCache<String, String> cache) {
        cache.put("sun", "sunday");
        cache.put("paper", "thesis");
    }
}
