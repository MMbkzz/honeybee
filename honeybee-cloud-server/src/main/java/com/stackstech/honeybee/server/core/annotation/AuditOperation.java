package com.stackstech.honeybee.server.core.annotation;


import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;

import java.lang.annotation.*;

/**
 * 审计操作注解
 *
 * @author William
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditOperation {

    String desc() default "";

    AuditOperationType type();

    AuditOperationType operation();

}
