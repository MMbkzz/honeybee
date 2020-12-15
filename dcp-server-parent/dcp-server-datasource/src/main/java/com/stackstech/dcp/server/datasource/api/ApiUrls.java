package com.stackstech.dcp.server.datasource.api;

/**
 * 资源类别（模块） API URI list:
 * 命名规范： /实体名复数/动作名_分割符
 */
public class ApiUrls {
    /**
     * root level URI
     */
    public static final String API_SERVICESOURCE_URI = "/api/servicesource";

    /**
     * add URI
     */
    public static final String API_SERVICESOURCE_ADD_URI = "/add_servicesource";

    /**
     * del URI
     */
    public static final String API_SERVICESOURCE_DELETE_URI = "/del_servicesource";

    /**
     * update URI
     */
    public static final String API_SERVICESOURCE_UPDATE_URI = "/edit_servicesource";

    /**
     * status URI
     */
    public static final String API_SERVICESOURCE_STATUS_URI = "/change_servicesource";

    /**
     * get all URI
     */
    public static final String API_SERVICESOURCE_QUERY_URI = "/query_servicesource";

    /**
     * get URI
     */
    public static final String API_SERVICESOURCE_GET_URI = "/get_servicesource";

    /**
     * check URI
     */
    public static final String API_SERVICESOURCE_CHECK_URI = "/check_servicesource";


    /**
     * get connection URI
     */
    public static final String API_SERVICESOURCE_CONNECTION_URI = "/get_servicesource_connnection";

    /**
     * get connection tables
     */
    public static final String API_SERVICESOURCE_TABLE_URI = "/get_servicesource_tables";

    /**
     * get connection fields
     */
    public static final String API_SERVICESOURCE_FIELD_URI = "/get_servicesource_fields";

    /**
     * execute connection sql
     */
    public static final String API_SERVICESOURCE_SQL_EXECUTE_URI = "/execute_servicesource_sql";

    /**
     * parse connection sql
     */
    public static final String API_SERVICESOURCE_SQL_PARSE_URI = "/parse_servicesource_sql";
    public static final String API_SERVICESOURCE_QUERY_ALL_URI = "/query_servicesource_all";
}
