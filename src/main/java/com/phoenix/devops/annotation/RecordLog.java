package com.phoenix.devops.annotation;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.enums.OperationType;
import com.phoenix.devops.parse.ContentParser;
import com.phoenix.devops.parse.DefaultContentParse;

import java.lang.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordLog {
    // 获取编辑信息的解析类，目前为使用id获取
    Class<? extends ContentParser> parseclass() default DefaultContentParse.class;

    // 查询数据库所调用的class文件 selectById方法所在的Service类
    Class<?> serviceclass() default IService.class;

    // 是否需要比较新旧数据
    boolean needDefaultCompare() default false;

    // id的类型
    Class<Long> idType() default Long.class;

    // 操作对象的id字段名称
    String primaryKey() default "id";

    // 操作类型 add update delete
    OperationType type() default OperationType.UPDATE;
}
