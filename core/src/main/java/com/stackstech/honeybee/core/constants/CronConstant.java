package com.stackstech.honeybee.core.constants;

public class CronConstant {

    /**
     * 节点 - 心跳包
     */
    public static String NODE_HEARTBEAT = "0/15 * * * * ?";
    /**
     * 节点 - 资源初始化/调整
     */
    public static String NODE_RESOURCE = "0 */1 * * * ?";
    /**
     * 节点 - 实例下线
     */
    public static String NODE_INSTANCE_OFFLINE = "0 */2 * * * ?";


    /**
     * 主数据 - 健康检查
     */
    public static String MAIN_HEALTH_CHECK = "0 */1 * * * ?";
    /**
     * 主数据 - 资源加载
     */
    public static String MAIN_RESOURCE_LOAD = "0 */1 * * * ?";
    /**
     * 主数据 - 实例加载
     */
    public static String MAIN_INSTANCE_LOAD = "0 */1 * * * ?";

    /**
     * 数据源-状态检查
     **/
    //public final static String MAIN_RESOURCE_CHECK = "0 */5 * * * ?";

    public final static String MAIN_RESOURCE_CHECK = "0 0 0/12 * * ?";


}
