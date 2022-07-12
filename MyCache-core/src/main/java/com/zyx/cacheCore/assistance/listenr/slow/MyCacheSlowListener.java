package com.zyx.cacheCore.assistance.listenr.slow;

import com.alibaba.fastjson.JSON;
import com.zyx.cacheApi.api.IMyCacheSlowListener;
import com.zyx.cacheApi.api.IMyCacheSlowListenerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 15:50
 * @Description
 */
@Slf4j
public class MyCacheSlowListener implements IMyCacheSlowListener {
    @Override
    public void listen(IMyCacheSlowListenerContext context) {
        log.warn("[Slow] methodName: {}, params: {}, cost time: {}",
                context.methodName(), JSON.toJSON(context.params()), context.costTimeMills());
    }

    @Override
    public long slowerThresholdMills() {
        return 1000L;
    }
}
