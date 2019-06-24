package com.chryl;

import com.chryl.oth.StorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@SpringBootApplication
@RestController
public class TestSpringbootUpdownApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSpringbootUpdownApplication.class, args);
    }

    /**
     * 单个文件上传
     */
    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("fileName") MultipartFile file) {
        String s = FileUtil.upLoadFile(file, "D:/test");
        if (Boolean.valueOf(s)) {
            return s;
        }
        return "false";
    }


    /**
     * 上传多个文件
     *
     * @param file1
     * @param file2
     * @return
     */
    @PostMapping("/batch")
    public String handleFileUpload(@RequestParam("fileName1") MultipartFile file1,//
                                   @RequestParam("fileName2") MultipartFile file2) {
        String path = "D:/test2";
        String s = FileUtil.upLoadFileS(path, file1, file2);
        if (Boolean.valueOf(s)) {
            return s;
        }
        return "false";
    }

    ///@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * 下载图片
     * 下载txt失败
     *
     * @param imgFile
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/downImg/{imgFile}")
    public String handleDownloadFile(@PathVariable String imgFile,//
                                     HttpServletResponse response) throws UnsupportedEncodingException {
        String path = "D:/test2";
        String s = FileUtil.downloadFile(path, imgFile, response);

        if (Boolean.valueOf(s)) {
            return s;
        }
        return "false";
    }


    /**
     * 下载
     */
    @Autowired
    private StorageServiceImpl storageService;

    @GetMapping("/downTxt/{txtFile}")
    public ResponseEntity<Resource> serveFile(@PathVariable String txtFile) throws UnsupportedEncodingException {
        Resource file = storageService.loadAsResource(txtFile);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


}
