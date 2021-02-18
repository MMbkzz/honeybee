package com.stackstech.honeybee.server.system.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountLoginVo {

    @NotBlank(message = "{account.login.username.valid}")
    private String account;

    @NotBlank(message = "{account.login.password.valid}")
    private String password;

}
