/**
 * Copyright 2019 the original author.
 *
 * @author William
 */
package com.stackstech.honeybee.server.core.handler;

import lombok.Data;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * API message i18n handler
 *
 * @author William
 * @since 1.0
 */
@Data
public final class MessageHandler {

    private ResourceBundleMessageSource messageSource;
    private Locale defaultLocale = Locale.SIMPLIFIED_CHINESE;
    private static MessageHandler handler;

    private MessageHandler() {
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/language");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
    }

    public String message(String code) {
        return messageSource.getMessage(code, null, defaultLocale);
    }

    public String message(String code, Object[] args) {
        return messageSource.getMessage(code, args, defaultLocale);
    }

    public static MessageHandler of() {
        if (handler == null) {
            handler = new MessageHandler();
        }
        return handler;
    }

}