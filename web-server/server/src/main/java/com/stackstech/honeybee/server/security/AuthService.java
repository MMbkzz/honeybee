package com.stackstech.honeybee.server.security;

import com.stackstech.honeybee.server.core.entity.AccountEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    AccountEntity login(HttpServletRequest request, HttpServletResponse response, String account, String password);

    void logout(HttpServletRequest request, HttpServletResponse response);

    boolean resetPassword(HttpServletRequest request, HttpServletResponse response, String account, String oldPassword, String newPassword);

    boolean verifyAccount(String token);

}
