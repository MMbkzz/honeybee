package com.stackstech.dcp.core.log;

public abstract class AbstractLoggerMethod {

    protected static final ThreadLocal<ServerLogger> LOCAL_LOG = new ThreadLocal<>();

    /**
     * 设置 Log 参数
     *
     * @param log
     */
    protected static void setLocalLog(ServerLogger log) {
        LOCAL_LOG.set(log);
    }

    /**
     * 获取 Log 参数
     *
     * @return
     */
    public static <T> ServerLogger<T> getLocalLog() {
        return LOCAL_LOG.get();
    }

    /**
     * 移除本地变量
     */
    public static void clearLog() {
        LOCAL_LOG.remove();
    }

}
