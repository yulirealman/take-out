package com.sky.controller.admin;

import com.sky.config.OssConfiguration;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
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
@ApiOperation("通用接口图片")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}", file);
        //todo create aliyun oss
//        try {
//            String originalFilename = file.getOriginalFilename();
//            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            String fileName = UUID.randomUUID().toString() + extension;
//            String path = aliOssUtil.upload(file.getBytes(), fileName);
//            return Result.success(path);
//        } catch (IOException e) {
//
//            log.error("文件上传失败：{}", e);
//        }
        String url = "https://static01.nyt.com/images/2024/12/31/multimedia/COOKING-HEALTHY-DINNERS-SALMON-BOWL-vmjp/COOKING-HEALTHY-DINNERS-SALMON-BOWL-vmjp-superJumbo.jpg?format=pjpg&quality=75&auto=webp&disable=upscale";
        return Result.success(url);
    }
}
