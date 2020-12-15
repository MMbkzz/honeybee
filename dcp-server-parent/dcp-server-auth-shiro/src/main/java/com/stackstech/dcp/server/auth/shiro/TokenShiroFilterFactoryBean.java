package com.stackstech.dcp.server.auth.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

public class TokenShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    public TokenShiroFilterFactoryBean() {
        super();
        super.getFilters().put("restToken", new TokenAccessControlFilter());
        super.getFilters().put("authcToken", new TokenAuthenticationFilter());
    }
}
