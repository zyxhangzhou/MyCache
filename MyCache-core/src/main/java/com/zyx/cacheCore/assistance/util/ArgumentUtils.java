package com.zyx.cacheCore.assistance.util;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/4 17:19
 * @Description
 */
public final class ArgumentUtils {
    private ArgumentUtils() {
    }

    public static void requireNotNull(Object object, String paramName) {
        if (null == object) {
            throw new IllegalArgumentException(paramName + " can not be null!");
        }
    }
    public static void requirePositve(int number, String paramName) {
        if (number < 0) {
            throw new IllegalArgumentException(paramName + " must be >= 0!");
        }
    }
}
