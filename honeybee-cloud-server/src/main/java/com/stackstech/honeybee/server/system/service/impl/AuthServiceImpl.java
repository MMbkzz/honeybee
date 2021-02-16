package com.stackstech.honeybee.server.system.service.impl;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.common.utils.AuthTokenBuilder;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.HttpHeader;
import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import com.stackstech.honeybee.server.core.exception.AuthenticationException;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.system.dao.AccountMapper;
import com.stackstech.honeybee.server.system.entity.AccountEntity;
import com.stackstech.honeybee.server.system.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AccountMapper mapper;
    @Autowired
    private AuthTokenBuilder authTokenBuilder;

    @Override
    public AccountEntity login(HttpServletRequest request, HttpServletResponse response, String account, String password) throws ServerException, DataNotFoundException {
        Map<String, Object> map = Maps.newHashMap();
        map.put("account", Optional.ofNullable(account).orElse("default"));
        map.put("password", Optional.ofNullable(password).orElse("default"));

        AccountEntity entity = mapper.selectByAccountAndPassowrd(map);
        if (entity == null) {
            throw new DataNotFoundException("login failed, please check your account and password");
        }
        String ip = CommonUtil.getRequestIpAddr(request);
        log.info("account login success, account id {}, login at {}", entity.getId(), ip);
        // generate auth token
        String currentToken = Optional.ofNullable(request.getHeader(HttpHeader.AUTHORIZATION)).orElse(null);
        authTokenBuilder.refreshAuthToken(currentToken, entity, response);
        return entity;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServerException {
        authTokenBuilder.destroyToken(request.getHeader(HttpHeader.AUTHORIZATION));
        log.info("account logout success");
    }

    @Override
    public boolean resetPassword(HttpServletRequest request, HttpServletResponse response, String account, String oldPassword, String newPassword) throws ServerException {
        Map<String, Object> map = Maps.newHashMap();
        map.put("account", Optional.ofNullable(account).orElse("default"));
        map.put("password", Optional.ofNullable(oldPassword).orElse("default"));

        AccountEntity entity = mapper.selectByAccountAndPassowrd(map);
        if (entity == null) {
            throw new DataNotFoundException("rest password failed, please check your account and password");
        }
        AccountEntity update = new AccountEntity();
        update.setId(entity.getId());
        update.setAccountName(account);
        update.setAccountPassword(newPassword);
        update.setUpdatetime(new Date());
        // update account password
        if (mapper.updateByPrimaryKeySelective(update) > 0) {
            // reissue auth token
            String currentToken = Optional.ofNullable(request.getHeader(HttpHeader.AUTHORIZATION)).orElse(null);
            authTokenBuilder.refreshAuthToken(currentToken, update, response);
            log.info("account rest password success, reissue the authentication token to the client");
            return true;
        }
        return false;
    }

    @Override
    public AccountEntity verifyAccount(String token) throws ServerException, AuthenticationException {
        AccountEntity account = authTokenBuilder.getAccount(token);
        if (account == null) {
            throw new AuthenticationException("authentication failed, invalid account");
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("account", account.getAccountName());
        map.put("password", account.getAccountPassword());
        account = mapper.selectByAccountAndPassowrd(map);
        if (account == null || account.getStatus() != EntityStatusType.ENABLE) {
            throw new AuthenticationException("authentication failed, please login");
        }
        return account;
    }

}
