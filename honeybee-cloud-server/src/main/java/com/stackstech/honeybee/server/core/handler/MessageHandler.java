/**
 * Copyright 2019 the original author.
 *
 * @author William
 */
package com.stackstech.honeybee.server.core.handler;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * API message i18n handler
 *
 * @author William
 * @since 1.0
 */
public final class MessageHandler {

    public static final String AUTH_LOGIN_FAILED = "auth.login.failed";
    public static final String AUTH_RESET_INFO_FAILED = "auth.reset.info.failed";
    public static final String AUTH_RESET_AUTHORITY_FAILED = "auth.reset.authority.failed";
    public static final String AUTH_TOKEN_ACCOUNT_INVALID = "auth.token.account.invalid";
    public static final String AUTH_TOKEN_ACCOUNT_FAILED = "auth.token.account.failed";
    public static final String ACCOUNT_NOT_FOUND = "account.not.found";
    public static final String ACCOUNT_UPDATE_FAILED = "account.update.failed";
    public static final String ACCOUNT_INSERT_FAILED = "account.insert.failed";
    public static final String ACCOUNT_LOGOUT_SUCCESS = "account.logout.success";
    public static final String ACCOUNT_RESET_SUCCESS = "account.reset.success";
    public static final String ACCOUNT_RESET_FAILED = "account.reset.failed";
    public static final String DATASOURCE_NOT_FOUND = "datasource.not.found";
    public static final String DATASOURCE_TYPE_INVALID = "datasource.type.invalid";
    public static final String DATASOURCE_UPDATE_FAILED = "datasource.update.failed";
    public static final String DATASOURCE_INSERT_FAILED = "datasource.insert.failed";
    public static final String CONFIG_VALUE_EMPTY = "config.value.empty";
    public static final String CONFIG_UPDATE_FAILED = "config.update.failed";

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