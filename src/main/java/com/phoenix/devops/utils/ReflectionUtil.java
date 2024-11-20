package com.phoenix.devops.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Slf4j
public final class ReflectionUtil {

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     *
     * @param obj       读取的对象
     * @param fieldName 读取的列
     * @return 属性值
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("不可能抛出的异常{}", e.getMessage());
            e.printStackTrace(System.err);
        }
        return result;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.如向上转型到Object仍无法找到, 返回null.
     *
     * @param obj       查找的对象
     * @param fieldName 列名
     * @return 列
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e) { // NOSONAR
                // Field不在当前类定义,继续向上转型
                e.printStackTrace(System.err);
                // new add
            }
        }
        return null;
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 获取两个对象同名属性内容不相同的列表
     *
     * @param class1 old对象
     * @param class2 new对象
     * @return 区别列表
     * @throws IllegalAccessException 异常
     */
    public static List<Map<String, Object>> compareTwoClass(Object class1, Object class2) throws IllegalAccessException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 获取对象的class
        Class<?> clazz1 = class1.getClass();
        Class<?> clazz2 = class2.getClass();
        // 获取对象的属性列表
        Field[] field1 = clazz1.getDeclaredFields();
        Field[] field2 = clazz2.getDeclaredFields();
        // 遍历属性列表field1
        for (Field value : field1) {
            // 遍历属性列表field2
            for (Field field : field2) {
                // 如果field1[i]属性名与field2[j]属性名内容相同
                if (value.getName().equals(field.getName())) {
                    value.setAccessible(true);
                    field.setAccessible(true);
                    // 如果field1[i]属性值与field2[j]属性值内容不相同
                    if (!compareTwo(value.get(class1), field.get(class2))) {
                        Map<String, Object> map2 = new HashMap<>();
                        Schema schema = value.getAnnotation(Schema.class);
                        String fieldName;
                        if (schema != null) {
                            fieldName = schema.description();
                        } else {
                            fieldName = value.getName();
                        }
                        map2.put("name", fieldName);
                        map2.put("old", value.get(class1));
                        map2.put("new", field.get(class2));
                        list.add(map2);
                    }
                    break;
                }
            }
        }
        return list;
    }

    /**
     * 对比两个数据是否内容相同
     *
     * @param object1 比较对象1
     * @param object2 比较对象2
     * @return boolean类型
     */
    public static boolean compareTwo(Object object1, Object object2) {
        if (object1 == null && object2 == null) {
            return true;
        }
        if (object1 == null) {
            return false;
        }
        return object1.equals(object2);
    }
}
