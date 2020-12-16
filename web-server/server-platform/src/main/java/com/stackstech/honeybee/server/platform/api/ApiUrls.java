package com.stackstech.honeybee.server.platform.api;

/**
 * 资源类别（模块） API URI list:
 * 命名规范： /实体名复数/动作名_分割符
 */
public class ApiUrls {
    /**
     * root level URI
     */
    public static final String API_SERVICEDRIVER_URI = "/api/platform/servicedriver";


    /**
     * add URI
     */
    public static final String API_SERVICEDRIVER_ADD_URL = "/add_servicedriver";


    /**
     * del URI
     */
    public static final String API_SERVICEDRIVER_DELETE_URI = "/del_servicedriver";

    /**
     * update URI
     */
    public static final String API_SERVICEDRIVER_UPDATE_URI = "/edit_servicedriver";

    /**
     * get all URI
     */
    public static final String API_SERVICEDRIVER_QUERY_URI = "/query_servicedriver";

    /**
     * get URI
     */
    public static final String API_SERVICEDRIVER_GET_URI = "/get_servicedriver";

    /**
     * check URI
     */
    public static final String API_SERVICEDRIVER_CHECK_URI = "/check_servicedriver";

    /**
     * status URI
     */
    public static final String API_SERVICEDRIVER_STATUS_URI = "/change_servicedriver_status";

    /**
     * Cache URI
     */
    public static final String API_CACHE_URI = "/api/platform/cache";

    public static final String API_CACHE_QUERY_URI = "/query_cache";

    public static final String API_CACHE_DEL_URI = "/del_cache";

    public static final String API_CACHE_GET_URI = "/get_cache";


    /**
     * cluster_operation_uri
     */
    public static final String API_OPERATION_URI = "/api/platform/operation";

    public static final String API_OPERATION_QUERY_URI = "/query_cluster_operation";

    public static final String API_OPERATION_ADD_URI = "/add_cluster_operation";

    public static final String API_OPERATION_UPDATE_URI = "/update_cluster_operation";

    public static final String API_OPERATION_DEL_URI = "/del_cluster_operation";

    public static final String API_OPERATION_STATUS_URI = "/change__cluster_operation";

    /**
     * Index URI
     */
    public static final String API_HOME_URI = "/api/platform/home";
    public static final String API_HOME_QUERY_URI = "/query_index";

    /**
     * SYS_CONFIG URI
     */
    public static final String API_CONFIG_URI = "/api/platform/config";
    public static final String API_CONFIG_QUERY_URI = "/query_config";
    public static final String API_CONFIG_ADD_URI = "/add_config";
    public static final String API_CONFIG_DEL_URI = "/del_config";
    public static final String API_CONFIG_EDIT_URI = "/update_config";
    public static final String API_CONFIG_QUERY_CONFIG_URI = "/query";

    /**
     * Message URI
     */
    public static final String API_MESSAGE_URI = "/api/platform/message";
    public static final String API_MESSAGE_QUERY_URI = "/query_message";
    public static final String API_MESSAGE_DEL_URI = "/del_message";
    public static final String API_MESSAGE_EDIT_URI = "/edit_message";
    public static final String API_MESSAGE_QUERY_WARN_URI = "/query_warn";
    public static final String API_MESSAGE_QUERY_NOTICE_URI = "/query_notice";
}
