package com.sky.config;


import com.sky.properties.R2Properties;
import com.sky.utils.R2OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class R2OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public R2OssUtil r2OssUtil(R2Properties r2Properties) {
        log.info("开始创建 Cloudflare R2 文件上传工具类对象: {}", r2Properties);
        return new R2OssUtil(
                r2Properties.getAccountId(),
                r2Properties.getAccessKeyId(),
                r2Properties.getSecretAccessKey(),
                r2Properties.getBucket()
        );
    }

}
