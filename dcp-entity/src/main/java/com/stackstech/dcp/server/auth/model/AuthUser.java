package com.stackstech.dcp.server.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stackstech.dcp.server.auth.model.vo.MenuVo;
import lombok.Data;

import java.util.List;

/**
 * 用户
 * 表名：dcp_auth_user
 */
@Data
public class AuthUser {
    /**
     * 用户编号
     */
    private String id;

    /**
     * ldap用户
     */
    private String ldapUser;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户性别
     */
    private String sex;


    @JsonIgnore
    private String password;

    @JsonIgnore
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
    private String previousLogin;

    /**
     * 创建时间
     */
    private String createTime;
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
    private String updateTime;
    /**
     * 修改人
     */
    private Long updateBy;

    /**
     * 用户来源
     */
    private String userSource;


    // vo
    /**
     * token
     */
    private String token;

    //角色code
    private String roles;

    /**
     * 菜单
     */
    private List<MenuVo> menu;

}