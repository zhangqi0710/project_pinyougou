package com.pinyougou.manager.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

@RestController
public class UploadController {
        @Value("${FILE_SERVER_URL}")
        private String FILE_SERVER_URL;//文件服务器地址
        @RequestMapping("/upload")
        public Result upload(MultipartFile file) {
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件扩展名
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        try {
            //创建客户端
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            //获取文件名称的字节码
            String fileId = client.uploadFile(file.getBytes(), extName);
            //图片完整地址
            String url = FILE_SERVER_URL + fileId;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
