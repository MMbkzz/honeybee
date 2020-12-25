package com.stackstech.honeybee.core.enums;

public enum ServiceSourceStateEnum {

    /**
     * 待发布
     */
    unpublished("unpublished", "待发布")
    /** 发布中 */
    , publishing("publishing", "发布中")
    /** 已发布 */
    , published("published", "已发布")
    /** 待下线 */
    , waiting("waiting", "待下线")
    /** 下线中 */
    , stopping("stopping", "下线中")
    /** 已下线 */
    , offline("offline", "已下线");

    public String code;
    public String desc;

    ServiceSourceStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
