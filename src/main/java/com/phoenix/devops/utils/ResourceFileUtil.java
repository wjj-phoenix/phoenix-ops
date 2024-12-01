package com.phoenix.devops.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author wjj-phoenix
 * @since 2024-12-01
 */
@Slf4j
public final class ResourceFileUtil {
    /**
     * 统计指定目录下的文件数量
     *
     * @param path 指定路径
     * @return 文件数量
     */
    public static long count(String path) {
        long count = 0;
        // 获取资源目录的URL
        URL resourceUrl = ResourceFileUtil.class.getClassLoader().getResource(path);
        Assert.notNull(resourceUrl, String.format("指定路径的资源目录不存在: %s", path));

        try {
            // 将URL转换为Path
            Path resourcePath = Paths.get(resourceUrl.toURI());

            // 检查是否是目录
            if (Files.isDirectory(resourcePath)) {
                // 获取目录下的所有文件和子目录
                try (Stream<Path> paths = Files.walk(resourcePath)) {
                    count = paths.filter(Files::isRegularFile).count();
                }
            } else {
                log.warn("指定的路径不是一个目录: {}", path);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return count;
    }
}
