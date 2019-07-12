package com.chryl.controller;

import com.chryl.service.StorageServiceImpl;
import com.chryl.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @RequestMapping------------------- , 一直没搞懂RequestMapping中consumes和produces的区别
 * 今天看了下源码，总结一下
 * <p>
 * 说到这两个参数，不得不先回顾一下HTTP协议Header中的两个东西
 * ContentType 和Accept
 * <p>
 * 在Request中
 * ContentType 用来告诉服务器当前发送的数据是什么格式
 * Accept      用来告诉服务器，客户端能认识哪些格式,最好返回这些格式中的其中一种
 * <p>
 * <p>
 * consumes 用来限制ContentType
 * produces 用来限制Accept
 * <p>
 * <p>
 * 举例:
 * 有个用户给我发了一个请求,
 * <p>
 * 请求头中
 * ContentType =application/json
 * Accept      =*斜杠*
 * <p>
 * 就是说用户发送的json格式的数据，可以接收任意格式的数据返回
 * <p>
 * 但是我的接口中定义了consumes={"application/xml"},produces={"application/xml"}
 * 我只接收 application/xml 格式，也只返回xml格式
 * <p>
 * 很明显，用户调不通这个接口
 * <p>
 * 所以我改下consumes={"application/xml","application/json"},produces={"application/xml"}
 * 注:除了格式支持，还需要与数据对应的http转换器（HttpMessageConverter）此处先跳过
 * <p>
 * <p>
 * MediaType 其实就是 application/xml,application/json 等类型格式
 * <p>
 * Created By Chr on 2019/7/12.
 */
@RestController
public class UpDownController {

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
//            fileName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
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
        //通用的MIME类型,以流的形式下载文件,实现任意格式的文件下载.
//        MIME(Multipurpose Internet Mail Extensions)多用途互联网邮件扩展类型。是设定某种扩展名的文件用一种应用程序来打开的方式类型，当该扩展名文件被访问的时候，浏览器会自动使用指定应用程序来打开。
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

    //自动关流
    public static void autoSD(HttpServletResponse resp, String path) {

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
