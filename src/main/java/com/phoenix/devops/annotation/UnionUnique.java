package com.phoenix.devops.annotation;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.validator.UnionUniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 * 联合唯一注解
 */
@Documented
@Target({ElementType.TYPE})
// 允许重复注解
@Repeatable(UnionUniques.class)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UnionUniqueValidator.class})
public @interface UnionUnique {
    /**
     * 查询数据库的服务接口
     *
     * @return 服务接口
     */
    Class<? extends IService<?>> service();

    /**
     * 组合字段
     *
     * @return 组合字段名称
     */
    String[] fields();

    /**
     * 错误提示信息
     *
     * @return 错误提示信息
     */
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
