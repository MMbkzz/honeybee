package com.stackstech.honeybee.core.enums;

/**
 * 项目名 sitemsg
 * <p>
 * 异常类型
 *
 * @author xiawang.liu
 * @date 2016年10月27日 下午2:50:15
 * @tel +86 138-2842-3701
 * @email xiawang.liu@hand-china.com
 */
public enum ErrorTypes {

    NULL_POINT("java.lang.NullPointerException");

    private String type;

    ErrorTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
