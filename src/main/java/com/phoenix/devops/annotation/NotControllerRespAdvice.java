package com.phoenix.devops.annotation;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotControllerRespAdvice {
}
