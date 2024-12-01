package com.phoenix.devops.utils;

import com.phoenix.devops.model.vo.PointVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 * 替换掉fastjson，自定义实现相关方法
 * note: 该实现不具有通用性，仅用于本项目。
 */
@Slf4j final
public class JsonUtil {
    public static List<PointVO> parseArray(String text, Class<PointVO> clazz) {
        if (text == null) {
            return null;
        } else {
            String[] arr = text.replaceFirst("\\[", "").replaceFirst("]", "").split("}");
            List<PointVO> ret = new ArrayList<>(arr.length);
            for (String s : arr) {
                ret.add(parseObject(s, PointVO.class));
            }
            return ret;
        }
    }

    public static PointVO parseObject(String text, Class<PointVO> clazz) {
        if (text == null) {
            return null;
        }

        try {
            PointVO ret = clazz.newInstance();
            return ret.parse(text);
        } catch (Exception ex) {
            log.error("json解析异常", ex);

        }
        return null;
    }

    public static String toJSONString(Object object) {
        switch (object) {
            case null -> {
                return "{}";
            }
            case PointVO t -> {
                return t.toJsonString();
            }
            case List ignored -> {
                List<PointVO> list = (List<PointVO>) object;
                StringBuilder buf = new StringBuilder("[");
                list.forEach(t -> {
                    buf.append(t.toJsonString()).append(",");
                });
                return buf.deleteCharAt(buf.lastIndexOf(",")).append("]").toString();
            }
            case Map map -> {
                return map.entrySet().toString();
            }
            default -> {
            }
        }
        throw new UnsupportedOperationException("不支持的输入类型:" + object.getClass().getSimpleName());
    }
}
