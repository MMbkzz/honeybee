package com.stackstech.dcp.server.dataasset.api;

/**
 * 资源类别（模块） API URI list:
 * 命名规范： /实体名复数/动作名_分割符
 */
public class ApiUrls {
    /**
     * dataasset area root  level URI
     */
    public static final String API_DATAASSET_AREA_URI = "/api/dataasset/area";

    /**
     * 增加资产领域
     */
    public static final String API_DATAASSET_ADD_AREA_URI = "/add_area";

    /**
     * 删除资产领域
     */
    public static final String API_DATAASSET_DEL_AREA_URI = "/del_area";

    /**
     * 修改资产领域
     */
    public static final String API_DATAASSET_EDIT_AREA_URI = "/edit_area";

    /**
     * 查询资产领域
     */
    public static final String API_DATAASSET_QUERY_AREA_URI = "/query_area";

    /**
     * 查询资产领域明细
     */
    public static final String API_DATAASSET_GET_AREA_URI = "/get_area";

    /**
     * 校验资产领域重名
     */
    public static final String API_DATAASSET_CHECK_AREA_URI = "/check_area";


    /**
     * dataasset topic  root  level URI
     */
    public static final String API_DATAASSET_TOPIC_URI = "/api/dataasset/topic";

    /**
     * 增加资产主题
     */
    public static final String API_DATAASSET_ADD_TOPIC_URI = "/add_topic";

    /**
     * 删除资产主题
     */
    public static final String API_DATAASSET_DEL_TOPIC_URI = "/del_topic";

    /**
     * 修改资产主题
     */
    public static final String API_DATAASSET_EDIT_TOPIC_URI = "/edit_topic";

    /**
     * 查询资产主题
     */
    public static final String API_DATAASSET_QUERY_TOPIC_URI = "/query_topic";

    /**
     * 查询资产主题明细
     */
    public static final String API_DATAASSET_GET_TOPIC_URI = "/get_topic";

    /**
     * 校验资产主题重名
     */
    public static final String API_DATAASSET_CHECK_TOPIC_URI = "/check_topic";

    /**
     * dataasset model  root  level URI
     */
    public static final String API_SERVICE_MODEL_URI = "/api/service/model";

    /**
     * 增加资产模型
     */
    public static final String API_SERVICE_ADD_MODEL_URI = "/add_model";

    /**
     * 批量增加资产模型
     */
    public static final String API_SERVICE_ADD_MODEL_BATCH_URI = "/add_models";

    /**
     * 删除资产模型
     */
    public static final String API_SERVICE_DEL_MODEL_URI = "/del_model";

    /**
     * 修改资产模型
     */
    public static final String API_SERVICE_EDIT_MODEL_URI = "/edit_model";

    /**
     * 查询资产模型
     */
    public static final String API_SERVICE_QUERY_MODEL_URI = "/query_model";

    /**
     * 校验资产模型
     */
    public static final String API_SERVICE_CHECK_MODEL_URI = "/check_model";

    /**
     * 校验模型参数类型
     */
    public static final String API_SERVICE_CHECK_MODEL_TYPE_URI = "/check_model_type";


    /**
     * 查询资产模型明细
     */
    public static final String API_SERVICE_GET_MODEL_URI = "/get_model";

    public static final String API_SERVICE_GET_PARENT_MODEL_URI = "/get_parent_model";

    /**
     * 增加资产模型字段
     */
    public static final String API_SERVICE_ADD_MODEL_FIELD_URI = "/add_model_field";

    /**
     * 删除资产模型字段
     */
    public static final String API_SERVICE_DEL_MODEL_FIELD_URI = "/del_model_field";

    /**
     * 修改资产模型字段
     */
    public static final String API_SERVICE_EDIT_MODEL_FIELD_URI = "/edit_model_field";

    /**
     * 查询资产模型字段
     */
    public static final String API_SERVICE_GET_MODEL_FIELD_URI = "/query_model_field";

    /**
     * 查询资产模型明细字段
     */
    public static final String API_SERVICE_GET_MODEL_FIELD_INFO_URI = "/get_model_field";

    /**
     * 增加资产模型字段
     */
    public static final String API_SERVICE_ADD_MODEL_PARAM_URI = "/add_model_param";

    /**
     * 删除资产模型字段
     */
    public static final String API_SERVICE_DEL_MODEL_PARAM_URI = "/del_model_param";

    /**
     * 修改资产模型字段
     */
    public static final String API_SERVICE_EDIT_MODEL_PARAM_URI = "/edit_model_param";

    /**
     * 查询资产模型字段
     */
    public static final String API_SERVICE_GET_MODEL_PARAM_URI = "/query_model_param";

    /**
     * 查询资产模型明细字段
     */
    public static final String API_SERVICE_GET_MODEL_PARAM_INFO_URI = "/get_model_param";

    /**
     * body模板解析
     */
    public static final String API_SERVICE_GET_MODEL_BODY_PATTERN_URI = "/get_body_pattern";

    /**
     * 资产快码查询 URI
     */
    public static final String API_MODELCODE_QUERY_URI = "/query_code";

    public static final String API_MODELCODE_QUERY_STATUS_URI = "/query_status";

    /**
     * 资产地图URI
     */
    public static final String API_DATAASSET_MAP_URI = "/api/dataasset/map";

    public static final String API_DATAASSET_MAP_QUERY_URI = "/query_map";


    // ================= 测试模块 开始 =================
    /**
     * 测试模块
     */
    public static final String API_BASE_URI = "/example";
    public static final String API_EXAMPLE_URI = API_BASE_URI + "/example";

    // ================= 测试模块 结束 =================
}
