package com.stackstech.honeybee.server.system.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@NotNull(message = "request parameter cannot be null")
public class RestPasswordVo {

    @NotNull(message = "account cannot be null")
    private String account;

    @NotNull(message = "old password cannot be null")
    private String oldPassword;

    @NotNull(message = "new password cannot be null")
    private String newPassword;

}
