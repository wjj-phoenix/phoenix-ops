package com.phoenix.devops;

import com.alibaba.druid.pool.DruidDataSource;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.dialect.IDialect;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
public class CodegenUtil {
    public static void main(String[] args) {
        // 配置数据源
        DruidDataSource dataSource = new DruidDataSource();
        Properties properties = CodegenUtil.readProperties();
        dataSource.setUrl(properties.getProperty("spring.datasource.url"));
        dataSource.setUsername(properties.getProperty("spring.datasource.username"));
        dataSource.setPassword(properties.getProperty("spring.datasource.password"));

        // 创建配置内容，两种风格都可以。
        GlobalConfig globalConfig = CodegenUtil.createGlobalConfigUseStyle();

        // 通过 datasource 和 globalConfig 创建代码生成器

        Generator generator = new Generator(dataSource, globalConfig, IDialect.MYSQL);

        // 生成代码
        generator.generate();
    }

    private static GlobalConfig createGlobalConfigUseStyle() {
        // 创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        // 设置根包
        globalConfig.setAuthor("wjj-phoenix");
        globalConfig.setSince(LocalDate.now().toString());
        globalConfig.setBasePackage(CodegenUtil.class.getPackage().getName());

        // 设置生成 entity 并启用 Lombok
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setOverwriteEnable(true)
                // 设置项目的JDK版本，项目的JDK为14及以上时建议设置该项，小于14则可以不设置
                // System.getProperty("java.specification.version") 能够在运行时获取jdk大版本号
                .setJdkVersion(Integer.parseInt(System.getProperty("java.specification.version")));

        globalConfig.enableTableDef().setOverwriteEnable(true);

        globalConfig.setMapperAnnotation(true);
        globalConfig.setMapperGenerateEnable(true);
        globalConfig.setMapperXmlGenerateEnable(true);

        globalConfig.setServiceClassPrefix("I");
        globalConfig.setServiceGenerateEnable(true);
        globalConfig.setServiceImplCacheExample(true);
        globalConfig.setServiceImplGenerateEnable(true);

        globalConfig.setControllerGenerateEnable(true);

        return globalConfig;
    }

    private static Properties readProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("application-dev.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
