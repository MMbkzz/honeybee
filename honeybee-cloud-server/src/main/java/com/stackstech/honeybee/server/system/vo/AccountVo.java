package com.stackstech.honeybee.server.system.vo;

import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AccountVo {

    @NotNull(message = "invalid account id", groups = {UpdateGroup.class})
    private Long id;

    @NotBlank(message = "account name cannot be null", groups = {AddGroup.class})
    private String accountName;

    @NotBlank(message = "account password cannot be null", groups = {AddGroup.class})
    private String accountPassword;

    // TODO role id
    private Long accountRole = -1L;

    private String accountRealname;

    private Integer accountGender;

    private String accountEmail;

    private String accountPhone;

    private Integer status;

    private String desc;

}
