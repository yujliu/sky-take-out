package com.sky.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
public class R2OssUtil {

    private String accountId;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传
     * @param bytes       文件字节
     * @param objectName  文件对象名（路径）
     * @return 文件访问 URL
     */
    public String upload(byte[] bytes, String objectName) {
        // 构建 S3Client（R2 兼容 S3 协议）
        S3Client s3 = S3Client.builder()
                .endpointOverride(URI.create("https://" + accountId + ".r2.cloudflarestorage.com"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, accessKeySecret)
                ))
                .region(Region.US_EAST_1)
                .build();

        try {
            // 上传文件
            s3.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectName)
                            .build(),
                    RequestBody.fromInputStream(new ByteArrayInputStream(bytes), bytes.length));

            // R2 公网访问地址（如果 bucket 设置了 public）
            String url = "https://pub-5de18690971849278dc937e0ef75db3b.r2.dev/"  + objectName;

            log.info("文件上传到: {}", url);
            return url;

        } catch (S3Exception e) {
            log.error("R2 上传失败，错误信息: {}", e.awsErrorDetails().errorMessage(), e);
            throw new RuntimeException("R2 上传失败", e);
        }
    }
}

