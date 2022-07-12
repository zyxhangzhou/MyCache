package com.zyx.cacheTest.listener;

import com.zyx.cacheApi.api.IMyCacheSlowListener;
import com.zyx.cacheApi.api.IMyCacheSlowListenerContext;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 15:32
 * @Description
 */
public class MySlowListener implements IMyCacheSlowListener {
    @Override
    public void listen(IMyCacheSlowListenerContext context) {
        System.out.println("【慢日志】name: " + context.methodName());
    }

    /**
     * 慢日志的阈值，设定为0是为了方便测试
     * @return long 设定为0
     */
    @Override
    public long slowerThresholdMills() {
        return 0;
    }
}
