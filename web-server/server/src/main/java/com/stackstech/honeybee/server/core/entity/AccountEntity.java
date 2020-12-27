package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AccountEntity {
    private Long id;

    private String accountName;

    private String accountPassword;

    private Long accountRole;

    private String accountRealname;

    private Integer accountGender;

    private String accountEmail;

    private String accountPhone;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}