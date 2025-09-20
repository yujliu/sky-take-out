package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.R2OssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "Common Interfaces")
@Slf4j
public class CommonController {

    @Autowired
    private R2OssUtil r2OssUtil;

    @PostMapping("/upload")
    @ApiOperation("upload file")
    public Result<String> upload(MultipartFile file){
        log.info("upload file:{}",file);

        try {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String objectName = UUID.randomUUID() + suffix;

            String filepath = r2OssUtil.upload(file.getBytes(),objectName);
            return Result.success(filepath);
        } catch (IOException e) {
            log.error("upload file error:");
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }





}
