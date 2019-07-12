package com.chryl.controller;

import com.chryl.utils.FileUtil;
import com.chryl.utils.IDUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 自定义上传
 */
@RestController
@RequestMapping("/pic")
public class UpDownController2 {

    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public String show(@RequestParam("uploadFile") MultipartFile uploadFile) {
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        //格式正确上传
        if (isLegal) {
            String s = FileUtil.upLoadFile(uploadFile, "D:/test/2019");
            if (Boolean.valueOf(s)) {
                return s;
            }
        }
        return "false";

    }


    public static void main(String args[]) {
        String sourceFileName = "idea-springboot热部署.png";
        Date nowDate = new Date();
        for (int x = 0; x < 100; x++) {

            // 生成新的文件名
            String fileName = new DateTime(nowDate).toString("yyyyMMddhhmmssSSSS")
                    + RandomUtils.nextInt(100, 9999) + "." + StringUtils.substringAfterLast(sourceFileName, ".");
            System.out.println(fileName);
        }

        System.out.println("==================");
        for (int x = 0; x < 100; x++) {
            System.out.println(IDUtils.genImageName());
        }
    }


}
