package com.stackstech.honeybee.data.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackstech.honeybee.data.core.enums.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 公共工具类
 *
 * @author William
 * @date 2019-03-01
 * @since 1.0
 */
@Slf4j
public final class CommonUtil {

    private static final Pattern EMAIL_REG = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    private static final Pattern PHONE_REG = Pattern.compile("^1[345789]\\d{9}$");

    /**
     * Jackson Json mapper
     */
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private CommonUtil() {
    }

    /**
     * 检查邮件格式
     *
     * @param email Email
     * @return boolean
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            flag = EMAIL_REG.matcher(email).matches();
        } catch (Exception e) {
            log.warn("", e);
        }
        return flag;
    }

    /**
     * 检查手机号码格式
     *
     * @param mobileNumber Mobile number
     * @return boolean
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            flag = PHONE_REG.matcher(mobileNumber).matches();
        } catch (Exception e) {
            log.warn("", e);
        }
        return flag;
    }

    /**
     * 创建新的验证码
     *
     * @return String
     */
    public static String createNewCode() {
        return Integer.toString(RandomUtils.nextInt(100000, 999999));
    }


    /**
     * Base64 解码
     *
     * @param value Base64 text
     * @return String
     */
    public static String decodeBase64(String value) {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(Base64.decodeBase64(value));
    }

    /**
     * Base64 编码
     *
     * @param value text
     * @return String
     */
    public static String encodeBase64(String value) {
        return Base64.encodeBase64URLSafeString(value.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 获取格式化的请求信息
     *
     * @param request HTTP Request
     * @return String
     */
    public static String getRequestHeaders(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getMethod());
        builder.append(" ");
        builder.append(request.getRequestURL());
        builder.append(" ");
        builder.append(request.getProtocol());
        builder.append(System.lineSeparator());
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String key = headers.nextElement();
            String value = request.getHeader(key);
            builder.append(key);
            builder.append(": ");
            builder.append(value);
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    /**
     * 解析请求客户端IP
     *
     * @param request HTTP Request
     * @return String
     */
    public static String getRequestIpAddr(HttpServletRequest request) {
        String value = Optional.ofNullable(request.getHeader("X-Forwarded-For")).orElse(request.getHeader("X-Real-IP"));
        String ip = value;
        if (StringUtils.isNotBlank(value)) {
            if (value.contains(",")) {
                ip = StringUtils.split(value, ",")[0];
            }
        } else {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取格式化的异常堆栈信息
     *
     * @param throwable Throwable
     * @return String
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(printWriter);
        } catch (Exception e) {
            log.error("", e);
        }
        return stringWriter.toString();
    }

    /**
     * JSON转对象
     *
     * @param json             JSON string
     * @param parametrized     Object class type
     * @param parameterClasses Parameter class type
     * @param <T>              Object class type
     * @return if error return null else return Object
     */
    public static <T> T jsonToObject(String json, Class<?> parametrized, Class<?>... parameterClasses) {
        try {
            JavaType valueType = JSON_MAPPER.getTypeFactory().constructParametricType(parametrized, parameterClasses);
            return JSON_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * JSON转对象
     *
     * @param json      JSON string
     * @param valueType Object class type
     * @param <T>       Object class type
     * @return if error return null else return Object
     */
    public static <T> T jsonToObject(String json, Class<T> valueType) {
        try {
            return JSON_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }


    /**
     * 输出异常堆栈信息到文件
     *
     * @param filePath  File path
     * @param throwable Throwable
     */
    public static void outputStackTraceFile(String filePath, Throwable throwable) {
        try (FileWriter fileWriter = new FileWriter(filePath, false)) {
            fileWriter.write(getStackTrace(throwable));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 对象转JSON
     *
     * @param obj Object
     * @return String
     */
    public static String toJsonString(Object obj) {
        try {
            return JSON_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("", e);
        }
        return null;
    }


    /**
     * 获取格式化的当前时间
     *
     * @return String
     */
    public static String now() {
        return now(Constant.FORMAT_DATETIME);
    }

    /**
     * 获取格式化的当前时间
     *
     * @param pattern DateTime format
     * @return String
     */
    public static String now(String pattern) {
        return DateTime.now().toString(pattern);
    }

    /**
     * 校验签名
     *
     * @param signature Signature
     * @param timestamp Timestamp
     * @param nonce     Nonce
     * @param token     Token
     * @return boolean
     */
    public static boolean checkSign(String signature, String timestamp, String nonce, String token) {
        String[] parms = new String[]{timestamp, nonce, token};
        Arrays.sort(parms);
        String sortString = StringUtils.join(parms);
        String hex = DigestUtils.sha1Hex(sortString.getBytes(StandardCharsets.UTF_8));
        return signature.equals(hex);
    }

}