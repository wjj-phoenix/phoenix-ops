package com.phoenix.devops.parse;

import com.mybatisflex.core.service.IService;
import com.phoenix.devops.annotation.RecordLog;
import com.phoenix.devops.utils.ReflectionUtil;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Service
public class DefaultContentParse implements ContentParser {
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public Object getResult(JoinPoint joinPoint,  RecordLog logger, String tableId) {
        Object info = joinPoint.getArgs()[0];
        Object id = ReflectionUtil.getFieldValue(info, tableId);
        Assert.notNull(id, "id不能为空");
        Class<Long> idType = logger.idType();
        if (idType.isInstance(id)) {
            Class<?> cls = logger.serviceclass();
            IService<?> service = (IService<?>) applicationContext.getBean(cls);
            return service.getById((Serializable) id);
        } else {
            throw new RuntimeException("请核实id type");
        }
    }
}
