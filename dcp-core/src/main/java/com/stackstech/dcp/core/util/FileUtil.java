package com.stackstech.dcp.core.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static boolean upload(InputStream in, String filePath, String fileName) throws IOException {
        boolean flag = false;
        String realPath = filePath + fileName;
        FileOutputStream out = null;
        try {
            File targetFile = new File(realPath);
            if (!targetFile.getParentFile().exists()) {
                boolean created = targetFile.getParentFile().mkdirs();
                logger.debug("The upload directory does not exist. operation status {}", created);
            }
            out = new FileOutputStream(filePath + fileName);
            int size = IOUtils.copy(in, out);
            flag = size != -1;
            logger.debug("Upload complete, {} bytes", size);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
            logger.debug("close all input and output");
        }
        return flag;
    }

    public static void download(String json, HttpServletRequest req, HttpServletResponse resp, String type) throws Exception {
        try {
            req.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持的文件输入编码格式。。");
        }

        ServletOutputStream output = null;
        BufferedOutputStream buff = null;
        try {
            if (StringUtils.isNotEmpty(json)) {
                //resp.setContentType("application/octet-stream");
                //resp.setHeader("Content-Type","application/octet-stream");
                resp.setContentType("application/" + type);
                resp.setHeader("Content-Type", "application/" + type);
                resp.setHeader("Content-Disposition", "attachment; " +
                        "filename=" + java.net.URLEncoder.encode("template", "UTF-8") + "." + type);

                output = resp.getOutputStream();
                buff = new BufferedOutputStream(output);
                buff.write(json.getBytes(StandardCharsets.UTF_8));
                buff.flush();
                buff.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (buff != null) {
                    buff.close();
                    buff = null;
                }
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
            try {
                if (output != null) {
                    output.close();
                    output = null;
                }
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }


    public static boolean deleteFile(String filePath, String fileName) {
        boolean flag = false;
        if (StringUtils.isAllEmpty(filePath, fileName)) {
            return false;
        }
        File file = new File(filePath + fileName);
        if (file.exists()) {
            flag = file.delete();
            logger.debug("Delete file {}, operation status {}", file.getPath(), flag);
        }
        return flag;
    }


}
