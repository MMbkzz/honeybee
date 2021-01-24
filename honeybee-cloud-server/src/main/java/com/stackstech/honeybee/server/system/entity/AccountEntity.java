package com.stackstech.honeybee.server.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.server.common.entity.DataEntity;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

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

    @Override
    public AccountEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return this;
    }

    @Override
    public AccountEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }
}