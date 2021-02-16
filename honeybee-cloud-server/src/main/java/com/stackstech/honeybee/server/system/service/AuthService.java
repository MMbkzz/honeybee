package com.stackstech.honeybee.server.system.service;

import com.stackstech.honeybee.server.core.exception.AuthenticationException;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
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
     * @throws ServerException
     * @throws DataNotFoundException
     */
    AccountEntity login(HttpServletRequest request, HttpServletResponse response, String account, String password) throws ServerException, DataNotFoundException;

    /**
     * Account logout
     *
     * @param request  HttpServlet request
     * @param response HttpServlet response
     * @throws ServerException
     */
    void logout(HttpServletRequest request, HttpServletResponse response) throws ServerException;

    /**
     * Rest account password
     *
     * @param request     HttpServlet request
     * @param response    HttpServlet response
     * @param account     account name
     * @param oldPassword account old password
     * @param newPassword account new password
     * @param owner       owner
     * @return boolean
     * @throws ServerException
     * @throws DataNotFoundException
     */
    boolean resetPassword(HttpServletRequest request, HttpServletResponse response, String account, String oldPassword, String newPassword, AccountEntity owner) throws ServerException;

    /**
     * verify request account
     *
     * @param token Authentication token
     * @return AccountEntity
     * @throws ServerException
     * @throws AuthenticationException
     */
    AccountEntity verifyAccount(String token) throws ServerException, AuthenticationException;

}
