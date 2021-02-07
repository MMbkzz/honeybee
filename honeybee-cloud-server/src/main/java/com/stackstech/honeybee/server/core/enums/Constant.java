package com.stackstech.honeybee.server.core.enums;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

/**
 * 全局常量定义
 *
 * @author William
 * @since 1.0
 */
public class Constant {

    /**
     * API Endpoint root
     */
    public static final String API_ENDPOINT_ROOT = "/api/v1";
    /**
     * URL路径分隔符
     */
    public static final String URL_SEPARATOR = "/";
    /**
     * 数据目录
     */
    public static final String DATA_PATH = "data";
    /**
     * 日志目录
     */
    public static final String LOG_PATH = "log";
    /**
     * 资源目录
     */
    public static final String RESOURCE_PATH = StringUtils.join(DATA_PATH, File.separatorChar, "resources");
    /**
     * 默认时区 GMT+8
     */
    public static final String TIME_ZONE = "GMT+8";
    /**
     * 0
     */
    public static final long MIN_VALUE = 0L;
    /**
     * 2^31 = 2147483648
     */
    public static final long MAX_VALUE = (long) Math.pow(2, 31);
    /**
     * yyyy-MM-dd
     */
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS Z
     */
    public static final String FORMAT_DATETIMES = "yyyy-MM-dd HH:mm:ss.SSS Z";
    /**
     * 通配符
     */
    public static final String WILDCARD = "*";
    /**
     * 默认字符串连接符
     */
    public static final String SEPARATOR = ":";
    /**
     * 最大请求内容大小{@code 3MB}
     */
    public static final int MAX_CONTENT_SIZE = 3145728;
    /**
     * No-store
     */
    public static final String NO_STORE = "no-store";
    /**
     * Access control max age time
     */
    public static final int ACCESS_CONTROL_MAX_AGE_TIME = 7200;
    /**
     * app server name
     */
    public static final String SERVER_NAME = "Honeybee";
    /**
     * app token prefix
     */
    public static final String TOKEN_PREFIX = "Bearer";
    /**
     * database sort field list
     */
    public static final List<String> SORTS = Lists.newArrayList("id", "status", "updatetime", "createtime");


}