package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountEntity extends DataEntity<AccountEntity> {

    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_NAME = "account";
    public static final String ACCOUNT_PWD = "password";

    private Long id;

    private String accountName;

    @JsonIgnore
    private String accountPassword;

    private Long accountRole;

    private String accountRealname;

    private Integer accountGender;

    private String accountEmail;

    private String accountPhone;

    private String desc;

}