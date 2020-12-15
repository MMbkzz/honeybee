package com.stackstech.honeybee.core.enums;

/**
 * 资源加载状态
 */
public enum ResourceLoadStatusEnum {

    /**
     * 运行中
     */
    running("running", "运行中")

    /** 执行完成 */
    , done("done", "执行完")

    /** 停止中 */
    , stopping("stopping", "停止中")

    /** 停止 */
    , stopped("stopped", "停止");

    public String code;
    public String desc;

    ResourceLoadStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
