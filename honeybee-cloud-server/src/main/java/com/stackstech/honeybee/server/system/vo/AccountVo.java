package com.stackstech.honeybee.server.system.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NotNull(message = "account parameter cannot be null")
public class AccountVo {

    private Long id;

    @NotNull(message = "account name cannot be null")
    private String accountName;

    @NotNull(message = "account password cannot be null")
    private String accountPassword;

    @Min(value = 1L, message = "invalid account role id")
    @NotNull(message = "account role id cannot be null")
    private Long accountRole;

    private String accountRealname;

    private Integer accountGender;

    private String accountEmail;

    private String accountPhone;

    private Integer status;

    private String desc;

}
