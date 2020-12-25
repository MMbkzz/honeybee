package com.stackstech.honeybee.core.enums;

/**
 * 实例阶段
 */
//TODO 太复杂，简化流程状态
public enum InstanceStageEnum {

    // 未激活
    unactivated("unactivated", "未激活")
    // 已激活
    , activated("activated", "已激活")
    // 已初始化
    , initialized("initialized", "已初始化")
    // 已注册
    , registered("registered", "已注册")
    // 已上线
    , online("online", "已上线")
    // 取消注册
    , unregister("unregister", "取消注册")
    // 释放资源
    , dealloc("dealloc", "释放资源")
    // 停止
    , stopped("stopped", "停止");


    public String code;
    public String desc;

    InstanceStageEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
