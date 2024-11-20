package com.phoenix.devops.annotation;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.validator.UniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueValidator.class})
public @interface Unique {
    // 查询数据库所调用的class文件
    Class<? extends IService<?>> service();

    /**
     * 字段名称
     *
     * @return 字段名
     */
    String field();

    /**
     * 校验失败时的错误信息
     *
     * @return str
     */
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
