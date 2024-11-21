package com.phoenix.devops.validator;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.phoenix.devops.annotation.Unique;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 单一字段唯一性校验
 *
 * @author wjj-phoenix
 * @since 2024-11-20
 * ConstraintValidator接口使用了泛型，需要指定两个参数：
 * - 第一个是自定义注解
 * - 第二个是需要校验的数据类型
 */
@Slf4j
@Component
public class UniqueValidator implements ConstraintValidator<Unique, String> {
    private Unique unique;
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 主要做一些初始化操作，它的参数是使用到的注解，可以获取到运行时的注解信息
     *
     * @param unique 自定义注解
     */
    @Override
    public void initialize(Unique unique) {
        this.unique = unique;
    }

    /**
     * 要实现的校验逻辑，被注解的对象会传入此方法中
     *
     * @param str                        需要检验的值
     * @param constraintValidatorContext 用于在执行自定义校验逻辑时向用户提供反馈
     * @return true|false
     */
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        String wrapperSql = String.format("%s=?", unique.field());
        IService<?> service = applicationContext.getBean(unique.service());
        return !service.exists(QueryWrapper.create().where(wrapperSql, str));
    }
}
