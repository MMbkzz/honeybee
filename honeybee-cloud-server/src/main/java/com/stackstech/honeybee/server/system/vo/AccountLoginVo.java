package com.stackstech.honeybee.server.system.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountLoginVo {

    @NotBlank(message = "{account.login.username}")
    private String account;

    @NotBlank(message = "password cannot be null")
    private String password;

}
