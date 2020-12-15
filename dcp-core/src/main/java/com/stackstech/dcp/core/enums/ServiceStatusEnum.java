package com.stackstech.dcp.core.enums;

/**
 * 服务&模型Enum
 */
public enum ServiceStatusEnum {

    //有效
    active,
    //无效
    inactive,
    //重负载均衡
    rebalance,
    //启用
    enabled,
    //禁用
    disabled,
    //删除
    deleted,
    //待发布
    unpublished,
    //发布中
    publishing,
    //已发布
    published,
    //待下线
    waiting,
    //下线中
    stopping,
    //已下线
    offline

}
