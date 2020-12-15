package com.stackstech.dcp.connector.core;

import java.util.Map;

public interface ResourceSessionFactory extends ResourceConfig {

    /**
     * 打开会话
     *
     * @param config
     * @return
     */
    ResourceSessionFactory openSession(ClassLoader classLoader, Map<String, Object> config);

    /**
     * 重新配置会话
     *
     * @param config
     * @return
     */
    ResourceSessionFactory reconfigure(ClassLoader classLoader, Map<String, Object> config);

    /**
     * 获取会话
     *
     * @return
     */
    ResourceSession getSession();

    /**
     * 获取配置
     *
     * @return
     */
    Map<String, Object> getConfiguration();

    /**
     * 关闭资源连接池
     */
    void close();

}
