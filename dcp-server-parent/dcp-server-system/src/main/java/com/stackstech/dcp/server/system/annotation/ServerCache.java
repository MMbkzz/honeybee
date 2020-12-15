package com.stackstech.dcp.server.system.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServerCache {

    /**
     * 缓存主键
     *
     * @return
     */
    String key();

    /**
     * 过期时间， 默认 0 毫秒
     *
     * @return
     */
    long expire() default 0;

    /**
     * 过期时间单位， 默认 毫秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
