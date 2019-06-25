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
     * 下载txt
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

    //day02@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * 下载 单个文件:txt或图片
     *
     * @param downloadName
     * @param resp
     * @return
     */
    @GetMapping("/down/{downloadName}")
    public String show(@PathVariable String downloadName, HttpServletResponse resp) {
        /**
         * 前端传来文件id,根据id查询文件信息
         */
//        AnnouncementAnnex announcementAnnex = announcementAnnexService.selectById(id);
        //真实文件名,数据库查出来
//        String name = announcementAnnex.getAnnexUrl();
        /**
         *下载的文件名字:两种情况
         * 1.前端传来的下载的文件名字
         * 2.数据库查出来的文件名字
         */
//        String downloadName=announcementAnnex.getAnnexName();

        //@param name         文件真实名字 ,就是存储的文件名字
        //@param downloadName 文件下载时名字 ,就是页面下载展示的名字
//        String name = "git笔记.txt";
        String name = ")0%JR1P2FU}17P1SN(`%$Q8.png";
        //        进行转码后的文件名，用来下载之后的文件名
        download(resp, name, downloadName);
        return "";
    }


    /**
     * 下载单个文件:txt或图片
     *
     * @param resp
     * @param name         文件真实名字 :存储的文件名字(服务器的文件名)-数据库查出来的
     * @param downloadName 文件下载时名字 :页面下载展示的名字(下载时的文件名字)-返回给用户下载的文件名字
     */
    public static void download(HttpServletResponse resp, String name, String downloadName) {
        String fileName = null;
        try {
            fileName = new String(downloadName.getBytes("GBK"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ///home/tomcat/apache-tomcat-9.0.1/files
//        String realPath = "D:" + File.separator + "apache-tomcat-8.5.15" + File.separator + "files";
        String realPath = "D:" + File.separator + "test2";
//        String realPath=File.separator+"home"+File.separator+"tomcat"+File.separator+"apache-tomcat-9.0.1"+File.separator+"files";
        String path = realPath + File.separator + name;
        File file = new File(path);
        resp.reset();
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("utf-8");
        resp.setContentLength((int) file.length());
        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        //非自动关流
        try {
            os = resp.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void autoSD(HttpServletResponse resp,String path) {
        //自动关流
        byte[] buff = new byte[1024];
        File file = new File(path);
        try (
                OutputStream os = resp.getOutputStream();
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))
        ) {
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
