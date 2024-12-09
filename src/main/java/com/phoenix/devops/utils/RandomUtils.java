package com.phoenix.devops.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wjj70
 */
public final class RandomUtils {
    /**
     * 随机范围内数字
     *
     * @param startNum 开始
     * @param endNum   结束
     * @return 随机数
     */
    public static Integer randomInt(int startNum, int endNum) {
        return ThreadLocalRandom.current().nextInt(endNum - startNum) + startNum;
    }

    /**
     * 获取随机字符串
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String randomStr(int length) {
        String str = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
