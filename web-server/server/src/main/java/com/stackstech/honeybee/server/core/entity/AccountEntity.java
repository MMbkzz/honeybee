package com.stackstech.honeybee.server.core.entity;

import java.util.Date;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public Long getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(Long accountRole) {
        this.accountRole = accountRole;
    }

    public String getAccountRealname() {
        return accountRealname;
    }

    public void setAccountRealname(String accountRealname) {
        this.accountRealname = accountRealname;
    }

    public Integer getAccountGender() {
        return accountGender;
    }

    public void setAccountGender(Integer accountGender) {
        this.accountGender = accountGender;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}