package com.chryl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * file工具类
 * <p>
 * Created By Chr on 2019/6/24.
 */
@Slf4j
public class FileUtil {

    /**
     * 单个文件上传
     */
    public static String upLoadFile(MultipartFile file, String path) {
        if (file.isEmpty()) {
            return "false";
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);

//        String path = "D:/test";
        File dest = new File(path + "/" + fileName);
        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            return "true";
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        }
    }

    /**
     * 上传多个文件
     *
     * @param path
     * @param f
     * @return
     */
    public static String upLoadFileS(String path, MultipartFile... f) {
        List<MultipartFile> files = new ArrayList<>();

        for (int i = 0; i < f.length; i++) {
            files.add(f[i]);

        }
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return "false";
            }
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            System.out.println(fileName + "-->" + size);

//            String path = "D:/test2";
            File dest = new File(path + "/" + fileName);
            if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest); //保存文件
            } catch (Exception e) {
                e.printStackTrace();
                return "false";
            }
        }
        return "true";
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param res      //     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
  /*
  public void downloadFile(String fileName, HttpServletResponse res) throws BusinessException, UnsupportedEncodingException {
        if (fileName == null) {
            throw new BusinessException("1001", "文件名不能为空");
        }

        // 通过文件名查找文件信息
        FileInfo fileInfo = fileInfoDao.findByFileName(fileName);
        log.info("fileInfo-->{}", fileInfo);
        if (fileInfo == null) {
            throw new BusinessException("2001", "文件名不存在");
        }

        //设置响应头
        res.setContentType("application/force-download");// 设置强制下载不打开
        res.addHeader("Content-Disposition", "attachment;fileName=" +
                new String(fileInfo.getFileOriginName().getBytes("gbk"), "iso8859-1"));// 设置文件名
        res.setHeader("Context-Type", "application/xmsdownload");

        //判断文件是否存在
        File file = new File(Paths.get(fileInfo.getFilePath(), fileName).toString());
        if (file.exists()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = res.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                log.info("下载成功");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("9999", e.getMessage());
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    */
    public static String downloadFile(String path, String fileName, HttpServletResponse res) throws UnsupportedEncodingException {
        if (fileName == null) {
            return "false";
        }
        //通过文件名查找文件信息fileInfo=selectByFileName(fileName);
        //查看文件信息,存储路径

        res.setContentType("application/force-download");// 设置强制下载不打开
        res.addHeader("Content-Disposition", //
                "attachment;fileName=" +
                        new String(fileName.getBytes("gbk"), "iso8859-1"));// 设置文件名

        res.setHeader("Context-Type", "application/xmsdownload");

        //判断文件是否存在
//        String path = "D:/test2";
        File file = new File(Paths.get(path, fileName).toString());
        if (file.exists()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = res.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                log.info("下载成功");
            } catch (Exception e) {
                e.printStackTrace();
                return "false";
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "true";
    }

}
