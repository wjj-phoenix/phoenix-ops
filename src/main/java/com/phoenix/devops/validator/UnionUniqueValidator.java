package com.phoenix.devops.validator;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.phoenix.devops.annotation.UnionUnique;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Slf4j
@Component
public class UnionUniqueValidator implements ConstraintValidator<UnionUnique, Object> {
    private UnionUnique unionUnique;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void initialize(UnionUnique unionUnique) {
        this.unionUnique = unionUnique;
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        String[] fields = unionUnique.fields();
        StringBuilder wrapperSql = new StringBuilder("1=1");
        List<Object> vals = new ArrayList<>();

        for (String fieldStr : fields) {
            // 使用反射机制获取字段对象
            Field field = ReflectionUtils.findField(obj.getClass(), fieldStr);
            Assert.notNull(field, "field " + fieldStr + " not found");
            ReflectionUtils.makeAccessible(field);

            // 拼接sql，值使用占位符方式，防止SQL注入
            wrapperSql.append(String.format(" and %s=? ", field.getName()));

            // 获取字段值
            vals.add(ReflectionUtils.getField(field, obj));
        }

        IService<?> service = applicationContext.getBean(unionUnique.service());
        // 调用存储库方法来检查唯一性（需要根据具体情况实现）
        return !service.exists(QueryWrapper.create().where(wrapperSql.toString(), vals.toArray()));
    }
}
