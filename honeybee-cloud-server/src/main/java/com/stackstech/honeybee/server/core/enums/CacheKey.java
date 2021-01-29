package com.stackstech.honeybee.server.core.enums;

/**
 * Redis缓存Key定义
 *
 * @author William
 * @date 2019-03-01
 * @since 1.0
 */
public enum CacheKey {
    /**
     * 用户会话
     */
    SESSION,
    /**
     * Token 黑名单
     */
    TOKEN_BLACKLIST,
    /**
     * 标记
     */
    FLAG,
    /**
     * 验证码
     */
    CODE,
    /**
     * 事务锁
     */
    LOCK,
    /**
     * 限量
     */
    LIMITED,
    /**
     * 小时
     */
    HOUR,
    /**
     * 每日
     */
    DAILY,
    /**
     * 总数
     */
    TOTAL,

    /**
     * 数据缓存
     */
    DATACACHE
}
