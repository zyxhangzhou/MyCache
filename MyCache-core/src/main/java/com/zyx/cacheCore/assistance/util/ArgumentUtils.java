package com.zyx.cacheCore.assistance.util;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 17:19
 * @Description 参数校验工具类
 */
public final class ArgumentUtils {
    private ArgumentUtils() {
    }

    public static void requireNotNull(Object object, String name) {
        if (null == object) {
            throw new IllegalArgumentException(name + " can not be null!");
        }
    }
    public static void requireNonNegative(int number, String name) {
        if (number < 0) {
            throw new IllegalArgumentException(name + " must be >= 0!");
        }
    }
}
