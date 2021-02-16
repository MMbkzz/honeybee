package com.stackstech.honeybee.server.system.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RestPasswordVo {

    @NotBlank(message = "account cannot be null")
    private String account;

    @NotBlank(message = "old password cannot be null")
    private String oldPassword;

    @NotBlank(message = "new password cannot be null")
    private String newPassword;

}
