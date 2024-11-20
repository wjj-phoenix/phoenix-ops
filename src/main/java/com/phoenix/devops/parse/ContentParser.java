package com.phoenix.devops.parse;

import com.phoenix.devops.annotation.RecordLog;
import org.aspectj.lang.JoinPoint;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
public interface ContentParser {

    /**
     * 获取信息返回查询出的对象
     *
     * @param joinPoint  查询条件的参数
     * @param operateLog 注解
     * @return 获得的结果
     */
    public Object getResult(JoinPoint joinPoint, RecordLog logger, String tableId);
}
