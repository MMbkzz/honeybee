package com.stackstech.honeybee.core.pdf;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public class PDFUtil {

    private static final Logger logger = LoggerFactory.getLogger(PDFUtil.class);

    /**
     * 根据模板类创建本地PDF文件
     *
     * @param dataMap      动态数据
     * @param templateName 模板名称
     * @param templatePath 模板地址
     * @param filePath     目标路径
     * @param fileName     文件名称
     */
    public static void createLocalWord(Map dataMap, String templateName, String templatePath, String filePath, String fileName) {
        try {
            //创建配置实例
            Configuration configuration = new Configuration();
            //设置编码
            configuration.setDefaultEncoding("UTF-8");
            //ftl模板文件
            configuration.setDirectoryForTemplateLoading(new File(templatePath));
            //获取模板
            Template template = configuration.getTemplate(templateName);

            //输出文件
            File outFile = new File(filePath + File.separator + fileName);
            //如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }

            //将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8));

            //生成文件
            template.process(dataMap, out);

            //关闭流
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 根据模板类创建PDF文件--浏览器弹出
     *
     * @param dataMap      参数
     * @param templatePath 模板文件地址
     * @param req
     * @param resp
     */
    public static void createWord(Map dataMap, String templatePath, HttpServletRequest req, HttpServletResponse resp) {
        try {
            //请求配置
            req.setCharacterEncoding("utf-8");
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Type", "application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment; " +
                    "filename=" + java.net.URLEncoder.encode("template", "UTF-8") + ".doc");

            //创建配置实例
            Configuration configuration = new Configuration();
            //设置编码
            configuration.setDefaultEncoding("UTF-8");
            //ftl模板文件
            configuration.setDirectoryForTemplateLoading(new File(templatePath));
            //获取模板
            Template template = configuration.getTemplate("freeMarker.ftl");
            //将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
            //生成文件
            template.process(dataMap, out);

            //关闭流
            out.flush();
            out.close();
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持的文件输入编码格式..", e);
        } catch (IOException e) {
            logger.error("读写模板类发生错误..", e);
        } catch (TemplateException e) {
            logger.error("模板格式错误..", e);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> result = getMap();

        Random r = new Random();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        StringBuffer sb = new StringBuffer();
        sb.append(sdf1.format(new Date()));
        sb.append("_");
        sb.append(r.nextInt(100));

        //文件路径
        String filePath = "D:/doc_f/";
        //文件唯一名称
        String fileOnlyName = "用freemarker生成Word文档_" + sb + ".doc";
        //文件名称
        String fileName = "用freemarker生成Word文档.doc";

        PDFUtil.createLocalWord(result, "freeMarker.ftl", "D:\\test\\", filePath, fileOnlyName);
        //PDFUtil.createPDF(result, new HttpServletRequest(), new HttpServletResponse());
    }

    private static Map<String, Object> getMap() {
        //基本信息
        Map<String, Object> params = new HashMap<>();
        params.put("serviceName", "服务111");
        params.put("serviceDesc", "服务111");
        params.put("requestMethod", "POST");
        params.put("url", "http://localhost:8080/api");
        params.put("requestType", "application/json");

        String testFormat = null;
        String dataFormat = testFormat != null ? String.valueOf(testFormat) : null;
        params.put("dataFormat", dataFormat);

        //参数列表
        List<Map<String, Object>> serviceModelParams = new ArrayList<>();
        if (serviceModelParams != null) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("name", "参数名称1");
            map1.put("type", "参数类型1");
            map1.put("desc", "参数描述1");
            map1.put("value", "wqwq");
            map1.put("required", "y");
            Map<String, Object> map2 = new HashMap<>();
            map2.put("name", "参数名称2");
            map2.put("type", "参数类型2");
            map2.put("desc", "参数描述2");
            map2.put("value", "你好");
            map2.put("required", "y");
            Map<String, Object> map3 = new HashMap<>();
            map3.put("name", "参数名称3");
            map3.put("type", "参数类型3");
            map3.put("desc", "参数描述3");
            map3.put("value", "你好sss");
            map3.put("required", "n");

            serviceModelParams.add(map1);
            serviceModelParams.add(map2);
            serviceModelParams.add(map3);
            params.put("appParams", serviceModelParams);
        }
        //授权信息
        List<Map<String, Object>> appUsers = new ArrayList<>();
        if (appUsers != null) {
            //授权用户信息
            params.put("userName", "用户名称1");
            params.put("token", "token11");
            params.put("appId", "appId11");
            params.put("dataServiceId", "serviceId11");
            List<Map<String, Object>> appDsFields = new ArrayList<>();
            if (appDsFields != null) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("fieldName", "字段名称1");
                map1.put("dataType", "字段类型1");
                map1.put("fieldDesc", "字段描述1");
                Map<String, Object> map2 = new HashMap<>();
                map2.put("fieldName", "字段名称1");
                map2.put("dataType", "字段类型1");
                map2.put("fieldDesc", "字段描述1");
                Map<String, Object> map3 = new HashMap<>();
                map3.put("fieldName", "字段名称1");
                map3.put("dataType", "字段类型1");
                map3.put("fieldDesc", "字段描述1");

                appDsFields.add(map1);
                appDsFields.add(map2);
                appDsFields.add(map3);
                params.put("appFields", appDsFields);
            }

            //响应参数
            String response = "{\"code\":200,\"message\":\"success\",\"data\":{\"result\":{\"返回数据\"}}}";
            params.put("responseMessage", response);
        }
        return params;
    }
}
