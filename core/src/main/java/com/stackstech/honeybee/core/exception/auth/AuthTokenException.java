package com.stackstech.honeybee.core.exception.auth;

public class AuthTokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static final int TOKEN_OK = 200;

    /**
     * ok
     *
     * @return
     */
    public static AuthTokenException ok() {
        return new AuthTokenException(TOKEN_OK);
    }

    /**
     * @return
     * @Title: nologin
     */
    public static AuthTokenException nologin() {
        return new AuthTokenException(4010, "用户没有登陆");
    }

    /**
     * @param message
     * @return
     * @Title: dataIllegal
     */
    public static AuthTokenException dataIllegal(String message) {
        return new AuthTokenException(4011, message);
    }

    /**
     * @return
     * @Title: missing
     */
    public static AuthTokenException missing() {
        return new AuthTokenException(4012, "缺少token");
    }

    /**
     * @param rootCause
     * @return
     * @Title: invalid
     */
    public static AuthTokenException invalid(Exception rootCause) {
        return new AuthTokenException(4013, "token失效", rootCause);
    }

    /**
     * @return
     * @Title: expired
     */
    public static AuthTokenException expired() {
        return new AuthTokenException(4014, "token时间过期");
    }

    public static Exception expired(Exception ex) {
        return new AuthTokenException(4014, "token过期异常", ex);
    }

    private final int code;

    public AuthTokenException(int code) {
        this.code = code;
    }

    public AuthTokenException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AuthTokenException(int code, String message, Exception rootCause) {
        super(message, rootCause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
