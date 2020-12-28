/**
 * Copyright 2019 the original author.
 *
 * @author William
 */
package com.stackstech.honeybee.server.core.enums;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 全局常量定义
 *
 * @author William
 * @date 2019-05-04
 * @since 1.0
 *
 */
public class Constant {

	/** URL路径分隔符 */
	public static final String URL_SEPARATOR = "/";
	/** 数据目录 */
	public static final String DATA_PATH = "data";
	/** 日志目录 */
	public static final String LOG_PATH = "logs";
	/** 资源目录 */
	public static final String RESOURCE_PATH = StringUtils.join(DATA_PATH, File.separatorChar, "resources");
	/** 图片目录名称 */
	public static final String IMAGE_PATH_NAME = "images";
	/** 图片目录 */
	public static final String IMAGE_PATH = StringUtils.join(RESOURCE_PATH, File.separatorChar, IMAGE_PATH_NAME);
	/** 封面图片地址 */
	public static final String COVER_IMG_URI = StringUtils.join(IMAGE_PATH_NAME, URL_SEPARATOR, "cover");
	/** 用户头像图片地址 */
	public static final String HEAD_IMG_URI = StringUtils.join(IMAGE_PATH_NAME, URL_SEPARATOR, "head");
	/** 临时文件目录 */
	public static final String TEMPFILE_PATH = StringUtils.join(DATA_PATH, File.separatorChar, "temp");
	/** 数据备份目录 */
	public static final String BACKUP_PATH = StringUtils.join(DATA_PATH, File.separatorChar, "backup");
	/** 索引数据目录 */
	public static final String INDEX_PATH = StringUtils.join(DATA_PATH, File.separatorChar, "index");
	/** 默认的图片扩展名{@code JPG} */
	public static final String DEFAULT_EXT = "jpg";
	/** GIF的图片扩展名{@code JPG} */
	public static final String GIF_EXT = "gif";
	/** 默认时区 GMT+8 */
	public static final String TIME_ZONE = "GMT+8";
	/** 0 */
	public static final long MIN_VALUE = 0L;
	/** 2^31 = 2147483648 */
	public static final long MAX_VALUE = (long) Math.pow(2, 31);
	/** yyyy-MM-dd */
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	/** yyyy-MM-dd HH:mm:ss */
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	/** yyyy-MM-dd HH:mm:ss.SSS Z */
	public static final String FORMAT_DATETIMES = "yyyy-MM-dd HH:mm:ss.SSS Z";
	/** 备份文件扩展名 */
	public static final String DATAFILE_EXT = ".dat";
	/** 通配符 */
	public static final String WILDCARD = "*";
	/** 默认字符串连接符 */
	public static final String SEPARATOR = ":";
	/** 最大请求内容大小{@code 3MB} */
	public static final int MAX_CONTENT_SIZE = 3145728;
	/** No-store */
	public static final String NO_STORE = "no-store";
	/** Access control max age time */
	public static final String ACCESS_CONTROL_MAX_AGE_TIME = "7200";
	/** Default JMX port */
	public static final String DEFAULT_JMX_PORT = "6666";
	/** Qike club name */
	public static final String QIKE_CLUB_NAME = "奇客圈";

}