package com.stackstech.dcp.core.enums;

/**
 * API访问日志
 */
public enum ApiLogEnum {

    //
    APP_ID,
    DATASERVICE_ID,
    //请求参数
    REQUEST_PARAMS,
    //访问开始时间
    ACCESS_START_TIME,
    //访问结束时间
    ACCESS_END_TIME,
    //DB执行开始时间
    DB_START_TIME,
    //DB执行结束时间
    DB_END_TIME,
    //实例IP
    INSTANCE_HOST,
    //端口
    INSTANCE_PORT,
    //客户端IP
    CLIENT_HOST,
    //返回状态码
    RETURN_CODE,
    //返回信息
    MESSAGE,
    //返回行数
    RETURN_ROW,
    //返回数据量大小
    RETURN_SIZE
}
