package com.stackstech.dcp.core.constants;

/**
 * 缓存键常量
 */
public class CacheKeyConstant {

    /**
     * api-server 实例心跳
     */
    public static final String CACHE_KEY_APISERVER_INSTANTCE_HEARTBEAT = "com:stackstech:dcp:apiserver:instance:heartbeat";

    /**
     * api-server 实例上线
     */
    public static final String CACHE_KEY_APISERVER_INSTANTCE_ONLINE = "com:stackstech:dcp:apiserver:instance:online";

    /**
     * api-server 实例下线
     */
    public static final String CACHE_KEY_APISERVER_INSTANTCE_OFFLINE = "com:stackstech:dcp:apiserver:instance:offline";

    /**
     * api-server 持有资源
     */
    public static final String CACHE_KEY_APISERVER_RESOURCE_HOLD = "com:stackstech:dcp:apiserver:resource:hold:%s";

    /**
     * api-server 预期资源
     */
    public static final String CACHE_KEY_APISERVER_RESOURCE_EXPECT = "com:stackstech:dcp:apiserver:resource:expect:%s";

    /**
     * api-server 预期资源执行状态
     */
    public static final String CACHE_KEY_APISERVER_RESOURCE_EXPECT_STATUS = "com:stackstech:dcp:apiserver:resource:expectStatus:%s";

    /**
     * api-server 请求服务日志数据缓存
     */
    public static final String CACHE_KEY_APISERVER_SERVICE_LOG = "com:stackstech:dcp:apiserver:service:log:%s";

    /**
     * api-server 请求服务数据缓存
     */
    public static final String CACHE_KEY_APISERVER_DATA_SERVICE = "com:stackstech:dcp:apiserver:service:%s";

    /**
     * api-server 活跃资源
     */
    public static final String CACHE_KEY_APISERVER_RESOURCE_ACTIVE = "com:stackstech:dcp:apiserver:resource:active:%s";

    /**
     * 用户登录缓存
     */
    public static final String CACHE_KEY_LOGIN_USER_TOKEN = "com:stackstech:dcp:server:user:token:%s";

    /**
     * 缓存key - 心跳包
     *
     * @return
     */
    public static String getHeartBeat() {
        return CACHE_KEY_APISERVER_INSTANTCE_HEARTBEAT;
    }

    /**
     * 缓存key - 上线实例
     *
     * @return
     */
    public static String getOnline() {
        return CACHE_KEY_APISERVER_INSTANTCE_ONLINE;
    }

    /**
     * 缓存key - 下线实例
     *
     * @return
     */
    public static String getOffline() {
        return CACHE_KEY_APISERVER_INSTANTCE_OFFLINE;
    }

    /**
     * 缓存key - 应用实例持有资源
     *
     * @param node 实例节点 127.0.0.1:8080
     * @return
     */
    public static String getResourceHold(String node) {
        return String.format(CACHE_KEY_APISERVER_RESOURCE_HOLD, node);
    }

    /**
     * 缓存key - 应用实例预期资源
     *
     * @param node 实例节点 127.0.0.1:8080
     * @return
     */
    public static String getResourceExpect(String node) {
        return String.format(CACHE_KEY_APISERVER_RESOURCE_EXPECT, node);
    }

    /**
     * 缓存key - 应用实例活跃资源
     *
     * @param node 实例节点 127.0.0.1:8080
     * @return
     */
    public static String getResourceActive(String node) {
        return String.format(CACHE_KEY_APISERVER_RESOURCE_ACTIVE, node);
    }

    /**
     * 缓存key - 应用实例预期资源执行状态
     *
     * @param node 实例节点 127.0.0.1:8080
     * @return
     */
    public static String getResourceExpectStatus(String node) {
        return String.format(CACHE_KEY_APISERVER_RESOURCE_EXPECT_STATUS, node);
    }

    /**
     * 缓存key - 日志数据key
     *
     * @param dataServiceId
     * @return
     */
    public static String getLogServiceKey(String dataServiceId) {
        return String.format(CACHE_KEY_APISERVER_SERVICE_LOG, dataServiceId);
    }

    public static String getDataServiceKey(String dataServiceId) {
        return String.format(CACHE_KEY_APISERVER_DATA_SERVICE, dataServiceId);
    }

    /**
     * 缓存key - 用户登录缓存
     *
     * @return
     */
    public static String getLoginUserToken(String token) {
        return String.format(CACHE_KEY_LOGIN_USER_TOKEN, token);
    }

}
