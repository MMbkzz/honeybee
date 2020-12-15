package com.stackstech.honeybee.server.dataservice.api;

/**
 * 数据服务 API URI list:
 * 命名规范： /实体名复数/动作名_分割符
 */
public class ApiUrls {

    /**
     * service root  level URI
     */
    public static final String API_SERVICE_URI = "/api/services";

    /**
     * 增加数据服务
     */
    public static final String API_SERVICE_ADD_URI = "/add_service";

    /**
     * 删除数据服务
     */
    public static final String API_SERVICE_DELETE_URI = "/del_service";

    /**
     * 修改数据服务
     */
    public static final String API_SERVICE_UPDATE_URI = "/edit_service";

    /**
     * 查询数据服务
     */
    public static final String API_SERVICE_QUERY_URI = "/query_service";

    /**
     * 查询数据服务列表
     */
    public static final String API_SERVICE_QUERY_AUTH_URI = "/query_auth_service";

    /**
     * 查询数据服务明细
     */
    public static final String API_SERVICE_GET_URI = "/get_service";


    /**
     * 重名校验
     */
    public static final String API_SERVICE_CHECK_URI = "/check_service";

    /**
     * 授权
     */
    public static final String API_SERVICE_AUTHORIZATION_URI = "/auth_service";

    /**
     * 验证
     */
    public static final String API_SERVICE_VERIFICATION_URI = "/verify_service";

    /**
     * 下载URI
     */
    public static final String API_SERVICE_VERIFY_DOWNLOAD_URI = "/download_service";

    /**
     * 更新状态
     */
    public static final String API_SERVICE_STATUS_URI = "/change_service_status";


    //-----appuser
    /**
     * 增加数据服务APP用户
     */
    public static final String API_SERVICE_USER_ADD_URI = "/add_app";

    /**
     * 删除数据服务APP用户
     */
    public static final String API_SERVICE_USER_DELETE_URI = "/del_app";

    /**
     * 修改数据服务APP用户
     */
    public static final String API_SERVICE_USER_UPDATE_URI = "/edit_app";

    /**
     * 查询数据服务APP用户
     */
    public static final String API_SERVICE_USER_QUERY_URI = "/query_app";

    /**
     * 查询数据服务明细APP用户
     */
    public static final String API_SERVICE_USER_GET_URI = "/get_app";

    /**
     * 重名校验
     */
    public static final String API_SERVICE_USER_CHECK_URI = "/check_app";

    /**
     * 授权APP
     */
    public static final String API_SERVICE__USER_AUTHORIZATION_URI = "/auth_app";

    /**
     * 查询授权App列表
     */
    public static final String API_SERVICE_USER_QUERY_AUTH_URI = "/query_auth_app";


    //-----app授权
    /**
     * 增加数据服务APP授权
     */
    public static final String API_SERVICE_DS_ADD_URI = "/add_ds";

    /**
     * 删除数据服务APP授权
     */
    public static final String API_SERVICE_DS_DELETE_URI = "/del_ds";

    /**
     * 修改数据服务APP授权
     */
    public static final String API_SERVICE_DS_UPDATE_URI = "/edit_ds";

    /**
     * 查询数据服务APP授权
     */
    public static final String API_SERVICE_DS_QUERY_URI = "/query_ds";

    /**
     * 查询数据服务明细APP授权
     */
    public static final String API_SERVICE_DS_GET_URI = "/get_ds";

    /**
     * 重名校验
     */
    public static final String API_SERVICE_DS_CHECK_URI = "/check_ds";

    //-----app授权字段
    /**
     * 增加数据服务APP授权字段
     */
    public static final String API_SERVICE_DS_FIELD_ADD_URI = "/add_ds_field";

    /**
     * 删除数据服务APP授权字段
     */
    public static final String API_SERVICE_DS_FIELD_DELETE_URI = "/del_ds_field";

    /**
     * 修改数据服务APP授权字段
     */
    public static final String API_SERVICE_DS_FIELD_UPDATE_URI = "/edit_ds_field";

    /**
     * 查询数据服务APP授权字段
     */
    public static final String API_SERVICE_DS_FIELD_QUERY_URI = "/query_ds_field";

    /**
     * 查询数据服务明细APP授权字段
     */
    public static final String API_SERVICE_DS_FIELD_GET_URI = "/get_ds_field";

    //数据服务访问日志
    /**
     * 增加数据服务APP授权字段
     */
    public static final String API_SERVICE_ACCESS_LOG_ADD_URI = "/add_access_log";

    /**
     * 删除数据服务APP授权字段
     */
    public static final String API_SERVICE_ACCESS_LOG_DELETE_URI = "/del_access_log";

    /**
     * 修改数据服务APP授权字段
     */
    public static final String API_SERVICE_ACCESS_LOG_UPDATE_URI = "/edit_access_log";

    /**
     * 查询数据服务APP授权字段
     */
    public static final String API_SERVICE_ACCESS_LOG_QUERY_URI = "/query_access_log";

    /**
     * 查询数据服务明细APP授权字段
     */
    public static final String API_SERVICE_ACCESS_LOG_GET_URI = "/get_access_log";

    // ================= 测试模块 开始 =================
    /**
     * 测试模块
     */
    public static final String API_EXAMPLE_URI = API_SERVICE_URI + "/example";

    // ================= 测试模块 结束 =================
}
