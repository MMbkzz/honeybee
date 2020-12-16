package com.stackstech.honeybee.server.operation.api;

/**
 * 资源类别（模块） API URI list:
 * 命名规范： /实体名复数/动作名_分割符
 */
public class ApiUrls {
    /**
     * root level URI
     */
    public static final String API_BASE_URI = "/api/operations";

    // 服务审计日志
    public static final String API_AUDIT_LOG_ADD_URI = "/operation_add";

    public static final String API_AUDIT_LOG_QUERY_URL = "/operation_query";

    public static final String API_AUDIT_LOG_GET_URL = "/operation_get";

    //  平台操作日志
    public static final String API_SYS_AUDIT_LOG_ADD_URI = "/sys_audit_log_add";

    public static final String API_SYS_AUDIT_LOG_QUERY_URL = "/sys_audit_log_query";

    public static final String API_SYS_AUDIT_LOG_GET_URL = "/sys_audit_log_get";

    //数据源监控
    public static final String API_SOURCE_MONITOR_QUERY_URI = "/query_source_monitor";

    //实例监控
    public static final String API_INSTANCE_MONITOR_QUERY_URI = "/query_instance_monitor";

    //服务监控
    public static final String API_SERVICE_MONITOR_QUERY_URI = "/query_service_monitor";
    public static final String API_SERVICE_MONITOR_QUERY_PAGE_URI = "/query_page_monitor";

}
