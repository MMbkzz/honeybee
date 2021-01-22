package com.stackstech.honeybee.server.core.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
@NotNull(message = "request parameter cannot be null")
public class AccountLoginVo {

    @NotNull(message = "account cannot be null")
    private String account;

    @NotNull(message = "password cannot be null")
    private String password;

}
