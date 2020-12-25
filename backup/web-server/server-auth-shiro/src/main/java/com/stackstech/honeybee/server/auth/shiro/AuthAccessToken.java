package com.stackstech.honeybee.server.auth.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthAccessToken implements AuthenticationToken {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 1L;

    private final String accessToken;

    private final String host;

    private final String agent;

    public AuthAccessToken(String accessToken, String host, String agent) {
        super();
        this.accessToken = accessToken;
        this.host = host;
        this.agent = agent;
        logger.debug("createToken, token {}, host {}, agent {}.", getPrincipal(), getHost(), getAgent());
    }

    @Override
    public Object getPrincipal() {
        return accessToken;
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    public String getHost() {
        return host;
    }

    public String getAgent() {
        return agent;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AuthAccessToken [accessToken=");
        builder.append(accessToken);
        builder.append(", host=");
        builder.append(host);
        builder.append(", agent=");
        builder.append(agent);
        builder.append("]");
        return builder.toString();
    }

}
