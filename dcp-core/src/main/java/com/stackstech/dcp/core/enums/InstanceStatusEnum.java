package com.stackstech.dcp.core.enums;

/**
 * 实例状态
 */
public enum InstanceStatusEnum {

    // 未知
    unknown("unknown", "未知")
    // 正常
    , normal("normal", "正常")
    // 待停止
    , stopping("stopping", "待停止")
    // 停止
    , stopped("stopped", "停止");


    public String code;
    public String desc;

    InstanceStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
