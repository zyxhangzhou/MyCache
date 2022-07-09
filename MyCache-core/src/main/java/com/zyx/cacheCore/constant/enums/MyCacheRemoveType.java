package com.zyx.cacheCore.constant.enums;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 14:51
 * @Description
 */
public enum MyCacheRemoveType {
    EXPIRE("expire", "过期"),
    EVICT("evict", "淘汰"),
    ;

    private final String code;
    private final String desc;

    MyCacheRemoveType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    @Override
    public String toString() {
        return "MyCacheRemoveType{" + "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
