package com.stackstech.dcp.server.auth.model.vo;

import com.stackstech.dcp.server.auth.model.AuthRole;

import java.util.Date;
import java.util.List;

public class UserVo {
    /**
     * 用户编号
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * ldap用户名
     */
    private String ldapUser;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 用户名，唯一
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 密码盐
     */
    private String passwdSalt;

    /**
     * 最后修改密码时间
     */
    private String lastPasswdChange;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 前一次登录时间
     */
    private Date previousLogin;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改人
     */
    private Long updateBy;

    /**
     * 用户来源
     */
    private String userSource;

    /**
     * 查询开始时间
     */
    private Date stratPreviousLogin;

    /**
     * 查询结束时间
     */
    private Date endPreviousLogin;

    /**
     * 所属组织描述
     */
    private String orgName;

    /**
     * 所属角色ID字符串用逗号隔开
     */
    private String roles;

    /**
     * 查询字段
     */
    private String queryString;

    /**
     * 所属角色对象
     */
    private List<AuthRole> roleList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPasswdSalt() {
        return passwdSalt;
    }

    public void setPasswdSalt(String passwdSalt) {
        this.passwdSalt = passwdSalt;
    }

    public String getLastPasswdChange() {
        return lastPasswdChange;
    }

    public void setLastPasswdChange(String lastPasswdChange) {
        this.lastPasswdChange = lastPasswdChange;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getPreviousLogin() {
        return previousLogin;
    }

    public void setPreviousLogin(Date previousLogin) {
        this.previousLogin = previousLogin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserSource() {
        return userSource;
    }

    public void setUserSource(String userSource) {
        this.userSource = userSource;
    }

    public Date getStratPreviousLogin() {
        return stratPreviousLogin;
    }

    public void setStratPreviousLogin(Date stratPreviousLogin) {
        this.stratPreviousLogin = stratPreviousLogin;
    }

    public Date getEndPreviousLogin() {
        return endPreviousLogin;
    }

    public void setEndPreviousLogin(Date endPreviousLogin) {
        this.endPreviousLogin = endPreviousLogin;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<AuthRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<AuthRole> roleList) {
        this.roleList = roleList;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getLdapUser() {
        return ldapUser;
    }

    public void setLdapUser(String ldapUser) {
        this.ldapUser = ldapUser;
    }
}
