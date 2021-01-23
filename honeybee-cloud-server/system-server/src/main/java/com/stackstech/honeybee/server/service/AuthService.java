package com.stackstech.honeybee.server.service;

import com.stackstech.honeybee.server.system.entity.AccountEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Account auth service
 *
 * @author William
 * @since 1.0
 */
public interface AuthService {

    /**
     * Account login
     *
     * @param request  HttpServlet request
     * @param response HttpServlet response
     * @param account  account name
     * @param password account password
     * @return AccountEntity
     */
    AccountEntity login(HttpServletRequest request, HttpServletResponse response, String account, String password);

    /**
     * Account logout
     *
     * @param request  HttpServlet request
     * @param response HttpServlet response
     */
    void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * Rest account password
     *
     * @param request     HttpServlet request
     * @param response    HttpServlet response
     * @param account     account name
     * @param oldPassword account old password
     * @param newPassword account new password
     * @return boolean
     */
    boolean resetPassword(HttpServletRequest request, HttpServletResponse response, String account, String oldPassword, String newPassword);

    /**
     * verify request account
     *
     * @param token Authentication token
     * @return AccountEntity
     */
    AccountEntity verifyAccount(String token);

}
