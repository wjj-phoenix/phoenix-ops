package com.phoenix.devops.utils;

import cn.hutool.core.util.ArrayUtil;
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

/**
 * @author wjj70
 */
@Slf4j
public final class ImageUtils {
    /**
     * 滑块底图
     */
    private static final Map<String, String> ORIGINAL_CACHE_MAP = new ConcurrentHashMap<>();
    /**
     * 滑块
     */
    private static final Map<String, String> SLIDING_BLOCK_CACHE_MAP = new ConcurrentHashMap<>();

    static {
        // 滑动拼图
        ORIGINAL_CACHE_MAP.putAll(fetchResourcesImagesFile("images/original"));
        SLIDING_BLOCK_CACHE_MAP.putAll(fetchResourcesImagesFile("images/slidingBlock"));
    }

    public static BufferedImage fetchOriginalImage() {
        String[] images = ORIGINAL_CACHE_MAP.keySet().toArray(new String[0]);
        if (ArrayUtil.isEmpty(images)) {
            throw new IllegalArgumentException("指定路径下不存在原图片!");
        }
        Integer randomInt = RandomUtils.randomInt(0, images.length);
        return base64StrToImage(ORIGINAL_CACHE_MAP.get(images[randomInt]));
    }

    public static String fetchSlidingBlockImage() {
        String[] images = SLIDING_BLOCK_CACHE_MAP.keySet().toArray(new String[0]);
        if (ArrayUtil.isEmpty(images)) {
            throw new IllegalArgumentException("指定路径下不存在滑块图片!");
        }
        Integer randomInt = RandomUtils.randomInt(0, images.length);
        return SLIDING_BLOCK_CACHE_MAP.get(images[randomInt]);
    }

    /**
     * base64 字符串转图片
     *
     * @param base64String base64String
     * @return 图片
     */
    public static BufferedImage base64StrToImage(String base64String) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(base64String);
        BufferedImage bufferedImage = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return bufferedImage;
    }


    private static Map<String, String> fetchResourcesImagesFile(String path) {
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
