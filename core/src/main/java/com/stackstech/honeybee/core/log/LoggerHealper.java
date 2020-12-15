package com.stackstech.honeybee.core.log;

import java.util.UUID;

public class LoggerHealper extends AbstractLoggerMethod {

    public static <T> void set(T log) {
        ServerLogger<T> logger = getLocalLog();

        if (null == logger) {
            Thread thread = Thread.currentThread();
            logger = new ServerLogger<>();
            logger.setThread(thread.getName() + " # " + UUID.randomUUID().toString().replace("-", ""));

            logger.setTimenano(System.currentTimeMillis());

            logger.setMessage(log);

            setLocalLog(logger);
        }
    }

    public static <T> T get() {
        return (T) getLocalLog().getMessage();
    }

}
