package com.stackstech.honeybee.server.auth.conf;

import com.stackstech.honeybee.server.auth.shiro.RedisAuthenticatingRealm;
import com.stackstech.honeybee.server.auth.shiro.TokenShiroFilterFactoryBean;
import com.stackstech.honeybee.server.auth.utils.LoginUserManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

public class AuthShiroConf {


    @Bean(name = "redisRealm")
    public RedisAuthenticatingRealm redisRealm(@Qualifier("loginUserManager") LoginUserManager loginUserManager) {
        RedisAuthenticatingRealm redisAuthenticatingRealm = new RedisAuthenticatingRealm();
        redisAuthenticatingRealm.setLoginUserManager(loginUserManager);
        return redisAuthenticatingRealm;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager createSecurityManager(@Qualifier("redisRealm") RedisAuthenticatingRealm redisAuthenticatingRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionMode(DefaultWebSecurityManager.NATIVE_SESSION_MODE);
        defaultWebSecurityManager.setRealm(redisAuthenticatingRealm);
        return defaultWebSecurityManager;
    }

    @Bean(name = "shiroFilter")
    public TokenShiroFilterFactoryBean createTokenShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        TokenShiroFilterFactoryBean tokenShiroFilterFactoryBean = new TokenShiroFilterFactoryBean();
        tokenShiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<String, String>();
        map.put("/**", "restToken");
        tokenShiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return tokenShiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor createAuthorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
