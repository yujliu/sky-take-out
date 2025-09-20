package com.sky.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "cloudflare.r2")
public class R2Properties {
    private String accountId;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucket;
    private String endpoint;
}
