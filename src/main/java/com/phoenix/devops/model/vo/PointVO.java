package com.phoenix.devops.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wjj70
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointVO {
    private String secretKey;

    public int x;

    public int y;

    public PointVO(int x, int y, String secretKey) {
        this.secretKey = secretKey;
        this.x = x;
        this.y = y;
    }

    public String toJsonString() {
        return String.format("{\"secretKey\":\"%s\",\"x\":%d,\"y\":%d}", secretKey, x, y);
    }

    public PointVO parse(String jsonStr) {
        Map<String, Object> m = new HashMap<>();
        Arrays.stream(jsonStr
                .replaceFirst(",\\{", "{")
                .replaceFirst("\\{", "")
                .replaceFirst("}", "")
                .replaceAll("\"", "")
                .split(",")).forEach(item -> m.put(item.split(":")[0], item.split(":")[1]));
        // PointVO d = new PointVO();
        setX(Double.valueOf(String.valueOf(m.getOrDefault("x", "0"))).intValue());
        setY(Double.valueOf(String.valueOf(m.getOrDefault("y", "0"))).intValue());
        setSecretKey(String.valueOf(m.getOrDefault("secretKey", "")));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PointVO pointVO = (PointVO) o;
        return x == pointVO.x && y == pointVO.y && Objects.equals(secretKey, pointVO.secretKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secretKey, x, y);
    }
}
