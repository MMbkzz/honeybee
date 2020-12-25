package com.stackstech.honeybee.server.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 登陆日志信息
 */
@Data
public class LoginInfo {

    private long id;                            //主键 ID
    private long userId;                       //用户ID
    private String loginName;                  //用户名
    private String loginIp;                    //登陆IP地址
    private String userAgent;                  //浏览器信息
    private String accessToken;                //票据
    private int tokenExpire;                   //过期时间单位（秒）
    private String roleNames;                       // 角色名
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginDate;                      //登陆时间

}
