package com.phoenix.devops.annotation;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 * 统一包裹验证注解
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnionUniques {
    UnionUnique[] value();
}
