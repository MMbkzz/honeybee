package com.stackstech.honeybee.server.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stackstech.honeybee.server.core.entity.AccountEntity;
import com.stackstech.honeybee.server.core.enums.Constant;
import com.stackstech.honeybee.server.core.enums.HttpHeader;
import com.stackstech.honeybee.server.core.enums.TokenStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

/**
 * @author william
 */
@Slf4j
@Component
public class AuthTokenBuilder {

    @Value("${app.security.token.issuer:honeybee}")
    private String issuer;

    @Value("${app.security.token.secret:honeybee}")
    private String secret;

    @Value("${app.security.token.expires:180}")
    private int expires;

    @Autowired
    private CacheUtil cacheUtil;

    /**
     * JWT algorithm
     */
    private Algorithm algorithm;

    private Algorithm getAlgorithm() {
        if (algorithm == null) {
            algorithm = Algorithm.HMAC256(secret);
        }
        return algorithm;
    }

    private DecodedJWT decodeToken(String token) throws JWTDecodeException {
        if (StringUtils.startsWith(token, Constant.TOKEN_PREFIX)) {
            token = StringUtils.substring(token, Constant.TOKEN_PREFIX.length()).trim();
        }
        String src = CommonUtil.decodeBase64(token);
        return JWT.decode(src);
    }

    private String encodeToken(String token) {
        return CommonUtil.encodeBase64(token);
    }

    protected JWTVerifier jwtVerifier() {
        return JWT.require(getAlgorithm()).withIssuer(issuer).build();
    }

    protected String generateTokenId() {
        return UUID.randomUUID().toString().replace("-", StringUtils.EMPTY);
    }


    public String generateToken(AccountEntity account) {
        Date nowTime = DateTime.now().toDate();
        Date expiresTime = DateTime.now().plusSeconds(expires).toDate();
        String tokenId = generateTokenId();
        // create new authentication token
        String token = JWT.create()
                .withJWTId(tokenId)
                .withClaim(AccountEntity.ACCOUNT_ID, account.getId())
                .withClaim(AccountEntity.ACCOUNT_NAME, account.getAccountName())
                .withClaim(AccountEntity.ACCOUNT_PWD, account.getAccountPassword())
                .withIssuer(issuer)
                .withIssuedAt(nowTime)
                .withExpiresAt(expiresTime)
                .sign(getAlgorithm());
        // encode token
        String newToken = encodeToken(token);
        log.info("Issue new authentication token {}, create at {}, expires at {}.", newToken, nowTime, expiresTime);
        return newToken;
    }

    public TokenStatus verifyToken(String token) {
        try {
            DecodedJWT jwt = decodeToken(token);
            if (cacheUtil.hasBlacklist(jwt.getId())) {
                return TokenStatus.INVALID;
            }
            jwtVerifier().verify(jwt);
        } catch (TokenExpiredException e) {
            log.debug("", e);
            return TokenStatus.EXPIRES;
        } catch (JWTVerificationException e1) {
            log.debug("", e1);
            return TokenStatus.INVALID;
        } catch (Exception e2) {
            log.error("", e2);
            return TokenStatus.INVALID;
        }
        return TokenStatus.VALID;
    }

    public void destroyToken(String token) {
        try {
            DecodedJWT jwt = decodeToken(token);
            // record token id to blacklist
            cacheUtil.addBlacklist(jwt.getId());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void refreshAuthToken(String currentToken, HttpServletResponse response) {
        try {
            // decode current token
            DecodedJWT jwt = decodeToken(currentToken);
            AccountEntity account = new AccountEntity();
            account.setId(jwt.getClaim(AccountEntity.ACCOUNT_ID).asLong());
            account.setAccountName(jwt.getClaim(AccountEntity.ACCOUNT_NAME).asString());
            account.setAccountPassword(jwt.getClaim(AccountEntity.ACCOUNT_PWD).asString());
            // generate new token
            this.refreshAuthToken(currentToken, account, response);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void refreshAuthToken(String currentToken, AccountEntity account, HttpServletResponse response) {
        String token = this.generateToken(account);
        this.refreshResponseHeader(token, response);
        if (StringUtils.isNotEmpty(currentToken)) {
            this.destroyToken(currentToken);
        }
    }

    protected void refreshResponseHeader(String token, HttpServletResponse response) {
        response.addHeader(HttpHeader.AUTHORIZATION, token);
        response.addHeader(HttpHeader.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeader.AUTHORIZATION);
        response.addHeader(HttpHeader.CACHE_CONTROL, Constant.NO_STORE);
    }

    @Deprecated
    public String getClaimValue(String token, String key) {
        DecodedJWT jwt = decodeToken(token);
        return jwt.getClaim(key).asString();
    }

}