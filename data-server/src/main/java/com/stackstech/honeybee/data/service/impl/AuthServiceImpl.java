package com.stackstech.honeybee.data.service.impl;

import com.stackstech.honeybee.data.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public boolean verifyAccount(String token) {
        //TODO
        return false;
    }
}
