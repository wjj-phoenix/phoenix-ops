package com.phoenix.devops.aspectj;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.phoenix.devops.annotation.RecordLog;
import com.phoenix.devops.entity.ChangeRecord;
import com.phoenix.devops.enums.OperationType;
import com.phoenix.devops.parse.ContentParser;
import com.phoenix.devops.utils.ReflectionUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Slf4j
@Aspect
@Component
public class RecordLogAspectj {
    @Resource
    private ApplicationContext applicationContext;

    // 保存修改之前的数据
    Map<String, Object> oldMap = new HashMap<>();

    @Before(value = "@annotation(operateLog)")
    public void boBefore(JoinPoint joinPoint, RecordLog operateLog) {
        ContentParser contentParser = applicationContext.getBean(operateLog.parseclass());
        // 旧值
        Object oldObject = contentParser.getResult(joinPoint, operateLog, operateLog.primaryKey());
        if (operateLog.needDefaultCompare()) {
            // 存储修改前的对象
            oldMap = objectToMap(oldObject);
        }
    }

    @After("@annotation(operateLog)")
    public void around(JoinPoint joinPoint, RecordLog operateLog) {
        ChangeRecord changeRecord = new ChangeRecord();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取请求的方法名
        String meName = method.getName();
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = className + "." + meName;
        log.info("切入点方法名: {}", methodName);
        // 请求uri
        String uri = request.getRequestURL().toString();
        log.info("请求路径: {}; 请求方法:{}", uri, request.getMethod());
        Object[] args = joinPoint.getArgs();
        // 请求参数 预留
        String requestParams = JSON.toJSONString(args);
        log.info("请求参数:{}", requestParams);
        // 创建人信息
        changeRecord.setChangeTime(LocalDateTime.now());
        // 操作账户
        // String account = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // log.info("操作用户: {}", account);

        ContentParser contentParser = applicationContext.getBean(operateLog.parseclass());
        Object object = contentParser.getResult(joinPoint, operateLog, operateLog.primaryKey());

        switch (operateLog.type()) {
            case OperationType.INSERT:
                Map<String, Object> dataMap = objectToMap(object);
                log.info("新增的数据: {}", dataMap.toString());
                break;
            case OperationType.UPDATE:
                if (operateLog.needDefaultCompare()) {
                    // 比较新数据与数据库原数据
                    List<Map<String, Object>> list = defaultDealUpdate(object, oldMap, operateLog.primaryKey());
                    for (Map<String, Object> map : list) {
                        changeRecord.setChangeField(String.valueOf(map.get("filedName")));
                        changeRecord.setBeforeChange(String.valueOf(map.get("oldValue")));
                        changeRecord.setAfterChange(String.valueOf(map.get("newValue")));
                        changeRecord.setTypeId(Long.parseLong(String.valueOf(map.get(operateLog.primaryKey()))));
                        String remark = "修改" + changeRecord.getChangeField() + "为" + changeRecord.getAfterChange() + ",原" + changeRecord.getChangeField() + "为" + (StrUtil.isBlank(changeRecord.getBeforeChange()) ? "空" : changeRecord.getBeforeChange());
                        changeRecord.setRemark(remark);
                    }
                }
            case OperationType.DELETE:
                log.info("删除的数据: {}", oldMap);
            default:
        }

        log.info("生成的日志:{}", changeRecord);
    }

    private List<Map<String, Object>> defaultDealUpdate(Object newObject, Map<String, Object> oldMap, String tableId) {
        log.info("旧数据:{}", oldMap);
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> newMap = objectToMap(newObject);
            oldMap.forEach((k, v) -> {
                Object newResult = newMap.get(k);
                if (null != v && !v.equals(newResult)) {
                    Field field = ReflectionUtil.getAccessibleField(newObject, k);
                    assert field != null;

                    Schema schema = field.getAnnotation(Schema.class);
                    if (null != schema && StrUtil.isNotEmpty(schema.description())) {
                        // 翻译表达式 0=男,1=女
                        Map<String, Object> result = new HashMap<>();
                        result.put("filedName", schema.description());
                        result.put(tableId, newMap.get(tableId));
                        result.put("oldValue", v);
                        result.put("newValue", newResult);

                        list.add(result);
                    }
                }
            });
            log.info("比较的数据:{}", list);
            return list;
        } catch (Exception e) {
            log.error("比较异常", e);
            e.printStackTrace(System.err);
            throw new RuntimeException("比较异常", e);
        }
    }

    private Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        // ObjectMapper mapper = new ObjectMapper();
        // mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 如果使用JPA请自己打开这条配置
        // mapper.addMixIn(Object.class, IgnoreHibernatePropertiesInJackson.class);
        return BeanUtil.beanToMap(obj);
    }

    /**
     * 翻译
     *
     * @param propertyValue 参数值如：0
     * @param converterExp  翻译注解的值如：0=男,1=女,2=未知
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StrUtil.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[0].equals(value)) {
                        propertyString.append(itemArray[1]).append(separator);
                        break;
                    }
                }
            } else {
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        return StrUtil.removeAll(propertyString.toString(), separator);
    }
}
