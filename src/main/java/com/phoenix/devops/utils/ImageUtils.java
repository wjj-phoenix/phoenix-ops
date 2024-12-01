package com.phoenix.devops.utils;

import cn.hutool.core.util.ArrayUtil;
import com.phoenix.devops.model.common.CaptchaBaseMapEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class ImageUtils {
    /**
     * 滑块底图
     */
    private static final Map<String, String> originalCacheMap = new ConcurrentHashMap<>();
    /**
     * 滑块
     */
    private static final Map<String, String> slidingBlockCacheMap = new ConcurrentHashMap<>();

    private static final Map<String, String[]> fileNameMap = new ConcurrentHashMap<>();

    static {
        // 滑动拼图
        originalCacheMap.putAll(getResourcesImagesFile("images/jigsaw/original"));
        slidingBlockCacheMap.putAll(getResourcesImagesFile("images/jigsaw/slidingBlock"));
        fileNameMap.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), originalCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), slidingBlockCacheMap.keySet().toArray(new String[0]));
        log.info("初始化底图:{}", JsonUtil.toJSONString(fileNameMap));
    }

    public static BufferedImage getOriginal() {
        String[] strings = fileNameMap.get(CaptchaBaseMapEnum.ORIGINAL.getCodeValue());
        if (ArrayUtil.isEmpty(strings)) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        return getBase64StrToImage(originalCacheMap.get(strings[randomInt]));
    }

    public static String getSlidingBlock() {
        String[] strings = fileNameMap.get(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue());
        if (ArrayUtil.isEmpty(strings)) {
            return null;
        }
        Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
        return slidingBlockCacheMap.get(strings[randomInt]);
    }

    /**
     * base64 字符串转图片
     *
     * @param base64String base64String
     * @return 图片
     */
    public static BufferedImage getBase64StrToImage(String base64String) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(base64String);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }


    private static Map<String, String> getResourcesImagesFile(String path) {
        // 默认提供六张底图
        Map<String, String> imgMap = new HashMap<>();
        for (long i = 1; i <= ResourceFileUtil.count(path); i++) {
            ClassPathResource resource = new ClassPathResource(path + "/" + i + ".png");
            byte[] bytes = new byte[0];
            try (InputStream inputStream = resource.getInputStream(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                bytes = byteArrayOutputStream.toByteArray();
            } catch (Exception ex) {
                log.error("error: {}", ex.getMessage());
                ex.printStackTrace(System.err);
            }
            String string = Base64Utils.encodeToString(bytes);
            String filename = String.valueOf(i).concat(".png");
            imgMap.put(filename, string);
        }
        return imgMap;
    }
}
