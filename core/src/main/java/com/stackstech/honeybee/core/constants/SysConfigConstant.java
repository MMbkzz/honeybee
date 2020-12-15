package com.stackstech.honeybee.core.constants;

public class SysConfigConstant {

    /**
     * 服务实例向主节点发送心跳包频率
     */
    public static Long INSTANCE_HEARTBEAT_INTERVAL = 60L;

    /**
     * 服务实例未知心跳超时时间
     */
    public static Long INSTANCE_HEARTBEAT_UNKNOW_TIMEOUT = 300L;

    /**
     * 服务实例待下线心跳超时时间
     */
    public static Long INSTANCE_HEARTBEAT_DECOMMISION_TIMEOUT = 600L;

    /**
     * 服务实例获取更新配置平率
     */
    public static Long INSTANCE_SETTINGCHECK_INTERVAL = 10L;


}
